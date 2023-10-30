package it.almaviva.impleme.prenotauffici.calendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.impleme.prenotauffici.counter.CounterEntity;
import it.almaviva.impleme.prenotauffici.counter.CounterService;
import it.almaviva.impleme.prenotauffici.office.OfficeService;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceEntity;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceService;
import it.almaviva.impleme.prenotauffici.public_service.ReservationType;
import it.almaviva.impleme.prenotauffici.reservation.ReservationService;
import it.almaviva.impleme.prenotauffici.reservation.ReservationStatus;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotMapper;
import it.almaviva.impleme.prenotauffici.timeslot.TimeslotService;

@Service
public class CalendarService {

    Logger logger = LoggerFactory.getLogger(CalendarService.class);

    @Autowired
    OfficeService officeService;
    @Autowired
    CounterService counterService;
    @Autowired
    PublicServiceService publicServcieService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    TimeslotService timeslotService;
    @Autowired
    TimeSlotMapper mapper;

    public CalendarDayDto getAvaiableSlots(UUID publicServiceId, Optional<String> municipalityId,
            Optional<UUID> officeId, LocalDate date) {

        PublicServiceEntity ps = publicServcieService.getById(publicServiceId);

        Map<Boolean, List<TimeSlotEntity>> timeslotsMap = timeslotService.getAll(municipalityId.get(), officeId.get())
                .stream().filter(timeslot -> isTimeSlotInDate(date, timeslot))
                .filter(timeslot -> hasFreeCounter(municipalityId.get(), officeId.get(), ps, date,
                        timeslot))
                .collect(Collectors.groupingBy((ts) -> ts.getDate() != null));

        // Remove standard timeslot when the they are ovverided by t
        Stream<TimeSlotEntity> stantardStream = timeslotsMap.getOrDefault(false, new ArrayList<>()).stream();
        for (TimeSlotEntity ts : timeslotsMap.getOrDefault(true, new ArrayList<>())) {
            System.out.println(ts.getId());
            stantardStream = stantardStream.filter(ts2 -> !timeslotService.areInConflict(ts, ts2));
        }
        List<TimeSlotEntity> timeslots = Stream.concat(stantardStream, timeslotsMap.getOrDefault(true, new ArrayList<>()).stream())
                .sorted((t1, t2) -> t1.getStartTime().compareTo(t2.getStartTime())).filter(timeslot -> timeslot.getReservable()).collect(Collectors.toList());

        // In political services the user should reserve
        // only the first avaiable timeslot of the day
        if (ReservationType.POLITICAL.equals(ps.getReservationType()) && timeslots.size() > 0) {
            timeslots = timeslots.subList(0, 1);
        }

        return new CalendarDayDto(date, mapper.map(timeslots));
    }


    public Boolean hasFreeCounter(String municipalityId, UUID officeId, PublicServiceEntity publicService, LocalDate date,
            TimeSlotEntity timeSlot) {
        Long numberOfCounter = numberOfCounterWithService(publicService.getId(), municipalityId, officeId);
        return reservationsForTimeSlot(municipalityId, officeId, date, publicService, timeSlot) < numberOfCounter;
    }

    public CalendarDto getAvaiableSlots(UUID publicServiceId, Optional<String> municipalityId, Optional<UUID> officeId,
            LocalDate startDate, LocalDate endDate) {

        List<CalendarDayDto> days = Stream
                // Iterate on days in date range
                .iterate(startDate, date -> endDate.isAfter(date) || endDate.isEqual(date), date -> date.plusDays(1))
                .map(date -> getAvaiableSlots(publicServiceId, municipalityId, officeId, date))
                .sorted((day1, day2) -> day1.getDate().compareTo(day2.getDate())).collect(Collectors.toList());

        return new CalendarDto(days);

    }

    private Long reservationsForTimeSlot(String municipalityId, UUID officeId, LocalDate date, PublicServiceEntity publicService,
            TimeSlotEntity timeslot) {

        return reservationService.getByTimeslotAndDateAndPubliService(timeslot, date, publicService).stream()
                .filter(reservation -> reservation.getStatus().equals(ReservationStatus.RICHIESTA) || reservation.getStatus().equals(ReservationStatus.CONFIRMED) )
                .count();
    }

    private Boolean isTimeSlotInDate(LocalDate date, TimeSlotEntity timeslot) {
        return null == timeslot.getDate() ? timeslot.getDayOfWeek().equals(date.getDayOfWeek())
                : date.isEqual(timeslot.getDate());
    }

    private Long numberOfCounterWithService(UUID publicServiceId, String municipalityId, UUID officeId) {
        return counterService.getAll(municipalityId, officeId).stream()
                .filter(counter -> counterHasService(counter, publicServiceId)).count();
    }

    private Boolean counterHasService(CounterEntity counter, UUID publicServiceId) {
        return counter.getPublicService().stream()
                .filter(currentService -> publicServiceId.equals(currentService.getId())).findAny().isPresent();
    }

}
