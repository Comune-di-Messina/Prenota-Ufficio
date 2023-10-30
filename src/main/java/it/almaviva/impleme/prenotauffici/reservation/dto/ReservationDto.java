package it.almaviva.impleme.prenotauffici.reservation.dto;


import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import it.almaviva.impleme.prenotauffici.office.dto.OfficeDto;
import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceDto;
import it.almaviva.impleme.prenotauffici.reservation.ReservationStatus;
import lombok.Data;

@Data
public class ReservationDto {
    private Long id;
    @Schema(type="string" , format = "time", example = "HH:mm")
    private LocalTime startTime;
    @Schema(type="string" , format = "time", example = "HH:mm")
    private LocalTime endTime;
    private OfficeDto office;
    private PublicServiceDto publicService;
    private LocalDate date;
    private ReservationStatus status;
    private String userId;
    private String qrcode;
    private String notes;
}
