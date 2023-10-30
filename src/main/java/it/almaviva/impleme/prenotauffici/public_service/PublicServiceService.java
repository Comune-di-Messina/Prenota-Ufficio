package it.almaviva.impleme.prenotauffici.public_service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.impleme.prenotauffici.common.Status;
import it.almaviva.impleme.prenotauffici.counter.CounterService;
import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceCreationDto;
import it.almaviva.impleme.prenotauffici.public_service.exception.PublicServiceNotFound;
import it.almaviva.impleme.prenotauffici.reservation.ReservationService;
import it.almaviva.impleme.prenotauffici.service_type.ServiceTypeEntity;
import it.almaviva.impleme.prenotauffici.service_type.ServiceTypeService;

@Service
public class PublicServiceService {

    @Autowired
    PublicServiceRepository repository;

    @Autowired
    PublicServiceMapper mapper;

    @Autowired
    CounterService counterService;

    @Autowired
    ServiceTypeService typeService;

    @Autowired
    ReservationService reservationService;


    private Predicate<PublicServiceEntity> isActive = ps -> Status.ACTIVE.equals(ps.getStatus());

    public PublicServiceEntity create(PublicServiceCreationDto dto) {
        PublicServiceEntity entity = mapper.map(dto);
        ServiceTypeEntity serviceType = typeService.getById(dto.getTypeId());
        entity.setType(serviceType);
        entity.setStatus(Status.ACTIVE);

        return repository.save(entity);
    }

    public PublicServiceEntity getById(UUID id) {

        return repository.findById(id).map(ps -> isActive.test(ps) ? ps : null).orElseThrow(() -> new PublicServiceNotFound(id));
    }

    public List<PublicServiceEntity> getAll(Optional<UUID> serviceTypeId, Optional<UUID> officeId) {
        if (officeId.isEmpty() && serviceTypeId.isEmpty()) {
            return repository.findByStatus(Status.ACTIVE);
        } else if (serviceTypeId.isPresent() && officeId.isEmpty()) {

            ServiceTypeEntity type = typeService.getById(serviceTypeId.get());
            return repository.findByTypeAndStatus(type, Status.ACTIVE);

        } else if (serviceTypeId.isEmpty() && officeId.isPresent()) {

            return counterService.getAll("", officeId.get()).stream()
                    .flatMap(counter -> counter.getPublicService().stream()).distinct().filter(isActive).collect(Collectors.toList());

        } else {
            return counterService.getAll("", officeId.get()).stream()
                    .flatMap(counter -> counter.getPublicService().stream()).distinct().filter(isActive)
                    .filter(ps -> ps.getType().getId().equals(serviceTypeId.get())).collect(Collectors.toList());
        }
    }

    public PublicServiceEntity delete(UUID id) {
        PublicServiceEntity toDelete = getById(id);
        return delete(toDelete);
    }

    private PublicServiceEntity delete(PublicServiceEntity toDelete){
        reservationService.checkFutureReservationsByService(toDelete);
        counterService.removeService(toDelete);

        toDelete.setStatus(Status.DELETED);
        return repository.save(toDelete);
    }

    public List<PublicServiceEntity> deleteByServiceType(ServiceTypeEntity type){
        List<PublicServiceEntity> toDelete = repository.findByTypeAndStatus(type, Status.ACTIVE);
        toDelete.forEach(this::delete);
        return toDelete;
    } 

    public PublicServiceEntity update(UUID id, PublicServiceCreationDto dto) {        
        PublicServiceEntity entity = mapper.update(getById(id), dto);
        ServiceTypeEntity type = typeService.getById(dto.getTypeId());
        entity.setType(type);
        return repository.save(entity);
    }

}
