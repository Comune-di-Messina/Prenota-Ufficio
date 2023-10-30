package it.almaviva.impleme.prenotauffici.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReservationFull extends RuntimeException{
    public ReservationFull(){
        super("Reservation is full for this timeslot");
    }
}
