package it.almaviva.impleme.prenotauffici.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.impleme.prenotauffici.calendar.exception.BadDates;


@RestController
@RequestMapping("v1/calendar")
@CrossOrigin(origins = "*")
public class CalendarController {

    @Autowired
    CalendarService service;

    @GetMapping()
    public CalendarDto getReservableTimeslots(@RequestParam UUID publicServiceId,
            @RequestParam String municipalityId, @RequestParam UUID officeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
        
                startDate.ifPresent(d -> { if( d.isBefore(LocalDate.now(ZoneId.of("Europe/Berlin"))))    throw BadDates.startInThePast(); });
                endDate.ifPresent(d -> { if( d.isBefore(LocalDate.now(ZoneId.of("Europe/Berlin"))))    throw BadDates.endInThePast(); });

                endDate.ifPresent(ed -> {
                    if(startDate.isPresent() && startDate.get().isAfter(ed)) throw BadDates.endDateBeforeStartDate();
                });


      final CalendarDto avaiableSlots = service.getAvaiableSlots(publicServiceId, Optional.of(municipalityId), Optional.of(officeId), startDate.orElseGet(LocalDate::now), endDate.orElse(startDate.orElseGet(LocalDate::now).plusDays(5)));
      final Optional<CalendarDayDto> now = avaiableSlots.getDays().stream().filter(calendarDayDto -> calendarDayDto.getDate().isEqual(LocalDate.now(ZoneId.of("Europe/Berlin")))).findAny();
      if(now.isPresent()){
        now.get().getTimeslots().removeIf(timeSlotDto -> timeSlotDto.getStartTime().isBefore(LocalTime.now(ZoneId.of("Europe/Berlin"))));
      }

      return avaiableSlots;
    }

   

}
