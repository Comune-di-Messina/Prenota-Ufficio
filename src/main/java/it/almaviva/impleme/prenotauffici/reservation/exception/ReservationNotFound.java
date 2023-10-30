package it.almaviva.impleme.prenotauffici.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReservationNotFound extends RuntimeException {
    

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private Long reservationId;

    public ReservationNotFound(Long id){
        super(String.format("No Reservation found with id '%s' ", id));
        this.reservationId = id;
    }
}
