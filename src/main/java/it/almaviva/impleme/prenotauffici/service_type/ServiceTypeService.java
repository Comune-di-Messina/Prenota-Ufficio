package it.almaviva.impleme.prenotauffici.service_type;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.impleme.prenotauffici.common.Status;
import it.almaviva.impleme.prenotauffici.counter.CounterService;
import it.almaviva.impleme.prenotauffici.office.OfficeService;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceService;
import it.almaviva.impleme.prenotauffici.service_type.dto.ServiceTypeCreationDto;
import it.almaviva.impleme.prenotauffici.service_type.exception.ServiceTypeNotFound;

@Service
public class ServiceTypeService {

    @Autowired
    ServiceTypeRepository repository;

    @Autowired
    ServiceTypeMapper mapper;

    @Autowired
    PublicServiceService psService;

    @Autowired
    OfficeService officeService;

    @Autowired
    CounterService counterService;

    public ServiceTypeEntity create(ServiceTypeCreationDto dto) {
        ServiceTypeEntity entity = mapper.map(dto);
        entity.setStatus(Status.ACTIVE);

        return repository.save(entity);
    }

    public ServiceTypeEntity getById(UUID id) {

        return repository.findById(id).map(o -> (! Status.ACTIVE.equals(o.getStatus())) ? null : o).orElseThrow(() -> new ServiceTypeNotFound(id));
    }

    public List<ServiceTypeEntity> getAll(Optional<String> municipalityId) {

        if (municipalityId.isEmpty()) {
            return repository.findByStatus(Status.ACTIVE);
        } else {

            return officeService.getAll(municipalityId.get(), Optional.empty(), Optional.empty()).stream()
                    .flatMap(office -> counterService.getAll(municipalityId.get(), office.getId()).stream())
                    .flatMap(counter -> counter.getPublicService().stream()).distinct().map(ps -> ps.getType())
                    .distinct().filter(st -> Status.ACTIVE.equals(st.getStatus()) ).collect((Collectors.toList()));

        }

    }

    public ServiceTypeEntity delete(UUID id) {

        ServiceTypeEntity toDelete = getById(id);

        toDelete.setStatus(Status.DELETED);

        psService.deleteByServiceType(toDelete);

        return repository.save(toDelete);
    }

    public ServiceTypeEntity update(UUID id, ServiceTypeCreationDto dto) {
        ServiceTypeEntity entity = mapper.update(getById(id), dto);
        return repository.save(entity);
    }

}
