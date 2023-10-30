package it.almaviva.impleme.prenotauffici.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BeforeReservationConflict  extends RuntimeException{
  public BeforeReservationConflict(){
    super("Data prenotazione non valida.");
  }
}
