package it.almaviva.impleme.prenotauffici.office.exception;


import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfficeNotFound extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private UUID officeId;
    private String municipalityId;

    public OfficeNotFound(String municipalityId, UUID id){
        super(String.format("No Office found with id '%s' for municipality '%s' ", id, municipalityId));
        this.officeId = id;
        this.municipalityId = municipalityId;
    }
    
    
    
}
