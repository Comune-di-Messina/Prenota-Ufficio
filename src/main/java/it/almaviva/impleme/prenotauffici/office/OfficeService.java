package it.almaviva.impleme.prenotauffici.office;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.impleme.prenotauffici.common.Status;
import it.almaviva.impleme.prenotauffici.counter.CounterService;
import it.almaviva.impleme.prenotauffici.office.dto.OfficeCreationDto;
import it.almaviva.impleme.prenotauffici.office.exception.OfficeNotFound;
import it.almaviva.impleme.prenotauffici.reservation.ReservationService;
import it.almaviva.impleme.prenotauffici.timeslot.TimeslotService;

@Service
public class OfficeService {
    @Autowired
    OfficeMapper mapper;
    @Autowired
    OfficeRepository repository;
    @Autowired
    CounterService counterService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    TimeslotService timesloService;
    

    public OfficeEntity create(String municipalityID, OfficeCreationDto dto) {
        OfficeEntity officeToSave = mapper.map(dto);
        officeToSave.setMunicipalityId(municipalityID);
        officeToSave.setStatus(Status.ACTIVE);

        return repository.save(officeToSave);
    }

    public OfficeEntity getById(String municipalityId, UUID officeId) {
        return repository.findById(officeId).map(o -> (! Status.ACTIVE.equals(o.getStatus())) ? null : o).orElseThrow(() -> new OfficeNotFound(municipalityId, officeId));
    }

    public List<OfficeEntity> getAll(String municipalityId, Optional<UUID> serviceTypeId, Optional<UUID> serviceId) {
        List<OfficeEntity> offices = repository.findByMunicipalityIdAndStatus(municipalityId, Status.ACTIVE);
        if (serviceId.isEmpty() && serviceTypeId.isEmpty()) {
            return offices;

        } else if (serviceId.isEmpty()) {
            return offices.stream()
                    .filter(office -> counterService.getAll(municipalityId, office.getId()).stream()
                            .filter(c -> c.getPublicService().stream()
                                    .filter(s -> s.getType().getId().equals(serviceTypeId.get())).findAny().isPresent())
                            .findAny().isPresent()

                    ).collect(Collectors.toList());
        }

        return offices.stream()
                .filter(office -> counterService
                        .getAll(municipalityId, office.getId()).stream().filter(c -> c.getPublicService().stream()
                                .filter(s -> s.getId().equals(serviceId.get())).findAny().isPresent())
                        .findAny().isPresent()

                ).collect(Collectors.toList());

    }

    public OfficeEntity delete(String municipalityId, UUID officeId) {
        OfficeEntity officeToDelete = getById(municipalityId, officeId);

        reservationService.checkFutureReservationsByOffice(officeToDelete);
        counterService.deleteByOffice(officeToDelete);
        timesloService.deleteByOffice(officeToDelete);

        officeToDelete.setStatus(Status.DELETED);
        
        return repository.save(officeToDelete);
    }

    public OfficeEntity update(String municipalityId, UUID officeId, OfficeCreationDto dto) {
        OfficeEntity officeToSave = mapper.update(getById(municipalityId, officeId), dto);
        return repository.save(officeToSave);
    }
}
