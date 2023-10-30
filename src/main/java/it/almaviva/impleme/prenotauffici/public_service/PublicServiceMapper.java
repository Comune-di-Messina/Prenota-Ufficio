package it.almaviva.impleme.prenotauffici.public_service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceCreationDto;
import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceDto;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
        Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PublicServiceMapper {


    public PublicServiceEntity map (PublicServiceDto dto);

    public PublicServiceEntity map (PublicServiceCreationDto dto);

    @Mapping(target ="typeId", source =  "type.id" )
    public PublicServiceDto map (PublicServiceEntity entity);

    public List<PublicServiceDto> map (List<PublicServiceEntity> entities);

    public PublicServiceEntity update(@MappingTarget PublicServiceEntity old, PublicServiceCreationDto dto);
    
    
}
