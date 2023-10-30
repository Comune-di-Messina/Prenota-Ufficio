package it.almaviva.impleme.prenotauffici.calendar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadDates extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected BadDates(String message){
        super(message);
    }


    public static BadDates startInThePast(){
        return new BadDates("startDate cannot be before today");
    }

    public static BadDates endInThePast(){
        return new BadDates("endDate cannot be before today");
    }

    public static BadDates endDateBeforeStartDate(){
        return new BadDates("endDate cannot be before startDate");
    }
    
    
}