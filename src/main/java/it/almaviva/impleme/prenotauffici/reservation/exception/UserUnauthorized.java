package it.almaviva.impleme.prenotauffici.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserUnauthorized extends RuntimeException {
    public UserUnauthorized(){
        super("Utente non autorizzato");
    }
}
