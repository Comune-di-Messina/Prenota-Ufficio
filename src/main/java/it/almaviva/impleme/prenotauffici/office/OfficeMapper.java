package it.almaviva.impleme.prenotauffici.office;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import it.almaviva.impleme.prenotauffici.office.dto.OfficeCreationDto;
import it.almaviva.impleme.prenotauffici.office.dto.OfficeDto;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
    Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface OfficeMapper {
    
    @Mapping(target = "latitude", source = "coordinates.latitude")
	@Mapping(target = "longitude", source = "coordinates.longitude")
    OfficeEntity map (OfficeCreationDto dto);

    @Mapping(target = "latitude", source = "coordinates.latitude")
	@Mapping(target = "longitude", source = "coordinates.longitude")
    OfficeEntity map (OfficeDto dto);

    @Mapping(target = "coordinates.latitude", source = "latitude")
	@Mapping(target = "coordinates.longitude", source = "longitude")
    OfficeDto map (OfficeEntity entity);

    @IterableMapping(qualifiedByName ="map")
    List<OfficeDto> map (List<OfficeEntity> entity);

    @Mapping(target = "latitude", source = "coordinates.latitude")
	@Mapping(target = "longitude", source = "coordinates.longitude")
    public OfficeEntity update(@MappingTarget OfficeEntity old, OfficeCreationDto dto);



}
