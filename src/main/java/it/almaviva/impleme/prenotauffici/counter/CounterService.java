package it.almaviva.impleme.prenotauffici.counter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.impleme.prenotauffici.counter.dto.CounterCreationDto;
import it.almaviva.impleme.prenotauffici.counter.exception.CounterNotFound;
import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import it.almaviva.impleme.prenotauffici.office.OfficeService;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceEntity;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceService;
import it.almaviva.impleme.prenotauffici.reservation.ReservationEntity;
import it.almaviva.impleme.prenotauffici.reservation.ReservationService;
import it.almaviva.impleme.prenotauffici.reservation.exception.FutureReservationConflict;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;

@Service
public class CounterService {

    Logger logger = LoggerFactory.getLogger(CounterService.class);

    @Autowired
    CounterMapper mapper;

    @Autowired
    CounterRepository repository;

    @Autowired
    OfficeService officeService;

    @Autowired
    PublicServiceService publicServiceService;

    @Autowired
    ReservationService reservationService;


    public CounterEntity create(String municipalityId, UUID officeId, CounterCreationDto dto) {
        CounterEntity counterToCreate = mapper.map(dto);
        counterToCreate.setOffice(officeService.getById(municipalityId, officeId));
        Set<PublicServiceEntity> services = dto.getPublicServiceId().stream()
                .map(id -> publicServiceService.getById(id)).collect(Collectors.toSet());
        counterToCreate.setPublicService(services);

        return repository.save(counterToCreate);
    }

    public CounterEntity getById(String municipalityId, UUID officeId, UUID id){
        return repository.findById(id).orElseThrow(() -> new CounterNotFound(municipalityId, officeId, id));
    }

    public List<CounterEntity> getAll(String municipalityId, UUID officeId){
        OfficeEntity office = officeService.getById(municipalityId, officeId);
        return repository.findByOffice(office);
    }

    public CounterEntity update(String municipalityId, UUID officeId,  UUID id,  CounterCreationDto dto){
        CounterEntity counterToUpdate = mapper.update(getById(municipalityId, officeId, id), dto);
        counterToUpdate.setOffice(officeService.getById(municipalityId, officeId));
        Set<PublicServiceEntity> services = dto.getPublicServiceId().stream()
                .map(serviceId -> publicServiceService.getById(serviceId)).collect(Collectors.toSet());
        counterToUpdate.setPublicService(services);

        return repository.save(counterToUpdate);
    }


    public void deleteByOffice(OfficeEntity office ){
        List<CounterEntity> counters = repository.findByOffice(office);
        repository.deleteAll(counters);
    }

    @Transactional
    public CounterEntity delete(String municipalityId, UUID officeId,  UUID id){
        CounterEntity counterToDelete = getById(municipalityId, officeId, id);


        // Check if there are any future reservations that need the current counter 
        reservationService.getFutureReservationsByCounter(counterToDelete)
        .collect(Collectors.groupingBy(ReservationEntity::getDate, Collectors.groupingBy(ReservationEntity::getTimeslot, Collectors.groupingBy(ReservationEntity::getPublicService))))
        .values().stream()
        .flatMap(e -> e.values().stream())
        .flatMap(e -> e.entrySet().stream())
        .filter( e -> {
            List<CounterEntity> counters = repository.findByPublicService_IdAndOffice(e.getKey().getId(), counterToDelete.getOffice());
            return counters.size() <= e.getValue().size();
        })
        .findAny().ifPresent(r -> { throw new FutureReservationConflict();});

        
        // Excplicit call to size to force publicService List to be initialized by JPA
        int size = counterToDelete.getPublicService().size();
        logger.debug("The size is {}", size);
        
        repository.deleteById(id);

        return counterToDelete;
    }

    public void removeService(PublicServiceEntity ps){
        List<CounterEntity> counters = repository.findByPublicService_Id(ps.getId());

        for(CounterEntity c : counters){
            c.setPublicService(c.getPublicService().stream().filter(psc -> !ps.getId().equals(ps.getId())).collect(Collectors.toSet()));
        }
        repository.saveAll(counters);
    }

}
