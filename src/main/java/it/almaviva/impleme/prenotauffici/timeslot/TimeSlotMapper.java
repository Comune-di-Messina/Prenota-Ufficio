package it.almaviva.impleme.prenotauffici.timeslot;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import it.almaviva.impleme.prenotauffici.office.OfficeMapper;
import it.almaviva.impleme.prenotauffici.timeslot.dto.TimeSlotCreationDto;
import it.almaviva.impleme.prenotauffici.timeslot.dto.TimeSlotDto;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
    Stream.class }, uses =  { OfficeMapper.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TimeSlotMapper {


    public TimeSlotDto map(TimeSlotEntity entity);
    public List<TimeSlotDto> map(List<TimeSlotEntity> entity);
    public TimeSlotEntity map(TimeSlotCreationDto dto);
    public TimeSlotEntity map(TimeSlotDto dto);

    public TimeSlotEntity update(@MappingTarget TimeSlotEntity old, TimeSlotCreationDto dto);
    
    
}
