package it.almaviva.impleme.prenotauffici.reservation;

import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import it.almaviva.impleme.prenotauffici.office.OfficeMapper;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceMapper;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationCreationDto;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationDto;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationUpdateDto;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotMapper;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
    Stream.class }, uses =  {OfficeMapper.class, TimeSlotMapper.class, PublicServiceMapper.class},nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface ReservationMapper {

    public ReservationEntity map(ReservationCreationDto dto);

    @Mapping(target = "userId", source =  "user.fiscalCode" )
    @Mapping(target = "qrcode", source = "qrcode", qualifiedByName = "imageToString")
    public ReservationDto map(ReservationEntity entity);
    public List<ReservationDto> map(List<ReservationEntity> entity);

    public ReservationEntity update(@MappingTarget ReservationEntity old, ReservationUpdateDto dto);

    @Named("imageToString")
    public default String imageToString(byte[] image){
        return Base64.getEncoder().encodeToString(image);
    }
    
}
