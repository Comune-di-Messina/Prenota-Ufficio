package it.almaviva.impleme.prenotauffici.service_type;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import it.almaviva.impleme.prenotauffici.service_type.dto.ServiceTypeCreationDto;
import it.almaviva.impleme.prenotauffici.service_type.dto.ServiceTypeDto;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
        Stream.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ServiceTypeMapper {


    public ServiceTypeEntity map (ServiceTypeDto dto);

    public ServiceTypeEntity map (ServiceTypeCreationDto dto);
    public ServiceTypeDto map (ServiceTypeEntity entity);

    @IterableMapping(qualifiedByName = "map")
    public List<ServiceTypeDto> map (List<ServiceTypeEntity> entities);

    public ServiceTypeEntity update(@MappingTarget ServiceTypeEntity old, ServiceTypeCreationDto dto);
    
    
    
}
