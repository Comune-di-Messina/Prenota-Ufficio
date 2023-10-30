package it.almaviva.impleme.prenotauffici.counter.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CounterNotFound extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String municipalityId;
    private UUID officeId;
    private UUID id;

    public CounterNotFound(String municipalityId, UUID officeId, UUID id){
        super(String.format("No counter found in municipality '%s' and office '%s' with id '%s'", municipalityId, officeId, id));
        this.municipalityId = municipalityId;
        this.officeId = officeId;
        this.id = id;
    }
    
    
    
}
