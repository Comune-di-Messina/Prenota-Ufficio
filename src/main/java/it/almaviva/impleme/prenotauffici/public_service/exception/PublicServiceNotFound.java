package it.almaviva.impleme.prenotauffici.public_service.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PublicServiceNotFound extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private UUID serviceId;

    public PublicServiceNotFound(UUID id){
        super(String.format("No Service found with id '%s' ", id));
        this.serviceId = id;
    }
    
    
    
}
