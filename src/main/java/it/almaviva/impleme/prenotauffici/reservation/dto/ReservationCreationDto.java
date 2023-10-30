package it.almaviva.impleme.prenotauffici.reservation.dto;


import java.time.LocalDate;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReservationCreationDto {
    @NotNull(message = "date can not be null")
    private LocalDate date;
    @NotNull(message = "municipalityId can not be null")
    private String municipalityId;
    @NotNull(message = "officeId can not be null")
    private UUID officeId;
    @NotNull(message = "timeslotId can not be null")
    private UUID timeslotId;
    @NotNull(message = "publicServiceId can not be null")
    private UUID publicServiceId;
    @NotNull(message = "user can not be null")
    private ReservationUserCreationDto user;
    private String notes;

}
