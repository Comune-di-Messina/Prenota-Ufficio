package it.almaviva.impleme.prenotauffici.timeslot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;

@ResponseStatus(HttpStatus.CONFLICT)
public class TimeslotConflict extends RuntimeException {
    

    public TimeslotConflict(TimeSlotEntity timeslot){
        super(String.format("The timeslot on %s, which start at %s and end at %s is in conflict with another timeslot", timeslot.getDayOfWeek(), timeslot.getStartTime(), timeslot.getEndTime()));
    }


}
