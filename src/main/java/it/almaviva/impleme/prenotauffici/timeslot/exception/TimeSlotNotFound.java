package it.almaviva.impleme.prenotauffici.timeslot.exception;


import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Data;

@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TimeSlotNotFound extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String municipalityId;
    private UUID officeId;
    private UUID timeslotId;

    public TimeSlotNotFound(String municipalityId, UUID officeId, UUID id){
        super(String.format("No Timeslot found with id '%s' for office '%s' and municipality '%s' ", id, officeId, municipalityId));
        this.municipalityId = municipalityId;
        this.officeId = officeId;
        this.timeslotId = id;
    }
    
    
    
}
