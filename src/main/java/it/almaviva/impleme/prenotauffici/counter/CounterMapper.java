package it.almaviva.impleme.prenotauffici.counter;

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

import it.almaviva.impleme.prenotauffici.counter.dto.CounterCreationDto;
import it.almaviva.impleme.prenotauffici.counter.dto.CounterDto;
import it.almaviva.impleme.prenotauffici.office.OfficeMapper;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceMapper;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
    Stream.class },uses = {PublicServiceMapper.class, OfficeMapper.class},  nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CounterMapper {


    @Mapping(target ="officeId", source =  "office.id" )
    CounterDto map(CounterEntity entity);
    List<CounterDto> map(List<CounterEntity> entity);
    CounterEntity map(CounterDto dto);
    CounterEntity map(CounterCreationDto dto);

    public CounterEntity update(@MappingTarget CounterEntity old, CounterCreationDto dto);
    
    
}
