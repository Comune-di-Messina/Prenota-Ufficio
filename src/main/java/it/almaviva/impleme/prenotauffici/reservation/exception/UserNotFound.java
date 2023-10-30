package it.almaviva.impleme.prenotauffici.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException {
    

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private String userId;

    public UserNotFound(String id){
        super(String.format("No User found with id '%s' ", id));
        this.userId = id;
    }
}
