package it.almaviva.impleme.prenotauffici.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FutureReservationConflict extends RuntimeException{
    public FutureReservationConflict(){
        super("Future reservations are already associated to this item.");
    }
}
