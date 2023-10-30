package it.almaviva.impleme.prenotauffici.service_type.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServiceTypeNotFound extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private UUID serviceTypeId;

    public ServiceTypeNotFound(UUID id){
        super(String.format("No ServiceType found with id '%s' ", id));
        this.serviceTypeId = id;
    }
    
    
    
}
