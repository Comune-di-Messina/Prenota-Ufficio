package it.almaviva.impleme.prenotauffici.calendar;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
    private List<CalendarDayDto> days;
}
