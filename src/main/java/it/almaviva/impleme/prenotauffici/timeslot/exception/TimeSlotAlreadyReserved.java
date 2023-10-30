package it.almaviva.impleme.prenotauffici.timeslot.exception;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;
import lombok.Data;

@ResponseStatus(HttpStatus.CONFLICT)
@Data
public class TimeSlotAlreadyReserved extends RuntimeException {
    

    private TimeSlotEntity timeSlot;
    private LocalDate date;
    

    public TimeSlotAlreadyReserved(TimeSlotEntity timeslot, LocalDate date){
        super(String.format("The timeslot %s cannot be set 'not reservable' because it has active reservation on %s", timeslot.getId(), date));
        this.timeSlot = timeslot;
        this.date = date;
    }


}