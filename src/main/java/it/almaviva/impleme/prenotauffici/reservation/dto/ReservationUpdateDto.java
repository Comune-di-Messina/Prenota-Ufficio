package it.almaviva.impleme.prenotauffici.reservation.dto;

import it.almaviva.impleme.prenotauffici.reservation.ReservationStatus;
import lombok.Data;

@Data
public class ReservationUpdateDto {
    private ReservationStatus status;
    private String notes;

}
