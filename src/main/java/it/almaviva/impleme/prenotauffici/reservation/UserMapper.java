package it.almaviva.impleme.prenotauffici.reservation;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationUserCreationDto;

@Mapper(componentModel = "spring", imports = { Collectors.class, Optional.class, Collection.class,
    Stream.class }, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    public UserEntity map(ReservationUserCreationDto dto);

}
