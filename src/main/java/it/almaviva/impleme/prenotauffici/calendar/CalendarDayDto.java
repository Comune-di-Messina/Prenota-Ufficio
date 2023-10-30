package it.almaviva.impleme.prenotauffici.calendar;

import java.time.LocalDate;
import java.util.List;

import it.almaviva.impleme.prenotauffici.timeslot.dto.TimeSlotDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDayDto {
  private LocalDate date;
  private List<TimeSlotDto> timeslots;  
}
