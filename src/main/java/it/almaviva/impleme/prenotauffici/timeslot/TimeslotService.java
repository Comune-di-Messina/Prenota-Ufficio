package it.almaviva.impleme.prenotauffici.timeslot;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import it.almaviva.impleme.prenotauffici.office.OfficeService;
import it.almaviva.impleme.prenotauffici.reservation.ReservationEntity;
import it.almaviva.impleme.prenotauffici.reservation.ReservationService;
import it.almaviva.impleme.prenotauffici.timeslot.dto.MutipleTimeSlotCreationDto;
import it.almaviva.impleme.prenotauffici.timeslot.dto.TimeSlotCreationDto;
import it.almaviva.impleme.prenotauffici.timeslot.exception.CannotOverrideAlreadyReservedTimeslot;
import it.almaviva.impleme.prenotauffici.timeslot.exception.TimeSlotAlreadyReserved;
import it.almaviva.impleme.prenotauffici.timeslot.exception.TimeSlotNotFound;
import it.almaviva.impleme.prenotauffici.timeslot.exception.TimeslotConflict;

@Service
public class TimeslotService {

    Logger logger = LoggerFactory.getLogger(TimeslotService.class);

    @Autowired
    TimeslotRepository repository;

    @Autowired
    TimeSlotMapper mapper;

    @Autowired
    OfficeService officeService;
    @Autowired
    ReservationService reservationService;

    public TimeSlotEntity getById(String municipalityId, UUID officeId, UUID id) {
        return repository.findById(id).orElseThrow(() -> new TimeSlotNotFound(municipalityId, officeId, id));
    }

    public List<TimeSlotEntity> getAll(String municipalityId, UUID officeId) {
        OfficeEntity office = officeService.getById(municipalityId, officeId);
        return repository.findByOffice(office);
    }

    public TimeSlotEntity create(String municipalityId, UUID officeId, TimeSlotCreationDto dto) {
        OfficeEntity office = officeService.getById(municipalityId, officeId);
        TimeSlotEntity timeSlotToCreate = mapper.map(dto);
        timeSlotToCreate.setOffice(office);
        if (hasConflict(timeSlotToCreate)) {
            throw new TimeslotConflict(timeSlotToCreate);
        }
        return repository.save(timeSlotToCreate);
    }

    public TimeSlotEntity update(String municipalityId, UUID officeId, UUID id, TimeSlotCreationDto dto) {
        TimeSlotEntity old = repository.findById(id).orElseThrow(() -> new TimeSlotNotFound(municipalityId, officeId, id));
        boolean oldIsReservable = old.getReservable();
        TimeSlotEntity timeSlotToUpdate = mapper.update(old, dto);
        
        if (hasConflict(timeSlotToUpdate)) {
            throw new TimeslotConflict(timeSlotToUpdate);
        }
        
        // Cannot set unreservable timeslot with active future reservation
        if((!timeSlotToUpdate.getReservable()) && oldIsReservable){
            List<ReservationEntity> activeFutureReservation = reservationService.getFutureActiveReservationOnTimeslot(timeSlotToUpdate);
            if(activeFutureReservation.size() > 0){
                throw new TimeSlotAlreadyReserved(old, activeFutureReservation.get(0).getDate());
            }
        }
       

        return repository.save(timeSlotToUpdate);
    }

    public boolean hasConflict(TimeSlotEntity timeslot) {

        Stream<TimeSlotEntity> timeslotsStrem = Stream.concat(this.repository.findByDayOfWeekAndOfficeAndDate( timeslot.getDayOfWeek(), timeslot.getOffice(), timeslot.getDate()).stream(),
            timeslot.getDate() == null ? Stream.of() : this.repository.findByDayOfWeekAndOfficeAndDate( timeslot.getDayOfWeek(), timeslot.getOffice(), null).stream());

         
        return   timeslotsStrem.filter(t -> !t.getId().equals(timeslot.getId()))
            .filter(t -> {
                if (null != timeslot.getDate()) {
                    if (t.getDate() != null && t.getDate().isEqual(timeslot.getDate())) {
                        if (areInConflict(timeslot, t)) {
                            return true;
                        }
                    }
                    if(t.getDate() == null){
                        List<ReservationEntity> futureActiveReservation = reservationService.getFutureActiveReservationOnTimeslot(t)
                        .stream()
                        .filter(res -> res.getDate().isEqual(timeslot.getDate()))
                        .collect(Collectors.toList());
                        
                        if(futureActiveReservation.size() > 0 && areInConflict(timeslot, t)){
                            throw new CannotOverrideAlreadyReservedTimeslot(t, futureActiveReservation.get(0).getDate());
                        }
                    }
                    return false;
                } else {
                    if (t.getDate() != null) {
                        return false;
                    }
                    if (areInConflict(timeslot, t)) {
                        return true;
                    }
                    return false;
                }
            }).findAny().map(t ->
            { 
                logger.error("Timeslot {} is in conflict with {}",timeslot.getId(), t.getId());
                return t;
            }).isPresent();
    }

    public boolean areInConflict(TimeSlotEntity t1, TimeSlotEntity t2) {
        if( ((t1.getStartTime().isBefore(t2.getStartTime()) || t1.getStartTime().equals(t2.getStartTime())) && (t2.getStartTime().isBefore(t1.getEndTime()) ) ) 
        || ((t1.getStartTime().isBefore(t2.getEndTime()) ) && (t2.getEndTime().isBefore(t1.getEndTime()) || t2.getEndTime().equals(t1.getEndTime()) ) ) 
        || ((t2.getStartTime().isBefore(t1.getStartTime()) || t2.getStartTime().equals(t1.getStartTime())) && (t1.getStartTime().isBefore(t2.getEndTime()))) 
        || ((t2.getStartTime().isBefore(t1.getEndTime()) ) && (t1.getEndTime().isBefore(t2.getEndTime()) || t1.getEndTime().equals(t2.getEndTime()) ) ) )return true;
        
        return false;
    }

    public TimeSlotEntity delete(String municipalityId, UUID officeId, UUID id) {
        TimeSlotEntity timeSlotToDelete = getById(municipalityId, officeId, id);

        reservationService.removeTimeSlot(timeSlotToDelete);

        repository.deleteById(id);
        return timeSlotToDelete;
    }

    public List<TimeSlotEntity> create(String municipalityId, UUID officeId, MutipleTimeSlotCreationDto dto) {
        OfficeEntity office = officeService.getById(municipalityId, officeId);
        List<TimeSlotEntity> listToCreate = dto.getDays().stream().flatMap((day) -> {

            List<TimeSlotCreationDto> dtos = new ArrayList<>();

            Duration timeslotDuration = Duration.ofMinutes(dto.getServiceDuration() + dto.getContingency());
            LocalTime startTime = dto.getStartTime();

            while (startTime.plusMinutes(timeslotDuration.toMinutes()).isBefore(dto.getEndTime())) {
                TimeSlotCreationDto singleDto = new TimeSlotCreationDto();
                singleDto.setDayOfWeek(day);
                singleDto.setReservable(true);
                singleDto.setStartTime(startTime);
                singleDto.setEndTime(startTime.plusMinutes(timeslotDuration.toMinutes()));

                dtos.add(singleDto);

                startTime = startTime.plusMinutes(timeslotDuration.toMinutes());
            }

            return dtos.stream();

        }).map((TimeSlotCreationDto singleDto) -> {
            TimeSlotEntity timeSlotToCreate = mapper.map(singleDto);
            timeSlotToCreate.setOffice(office);
            if (hasConflict(timeSlotToCreate)) {
                throw new TimeslotConflict(timeSlotToCreate);
            }
            return timeSlotToCreate;
        }).collect(Collectors.toList());

        return repository.saveAll(listToCreate);
    }

    public void deleteByOffice(OfficeEntity office) {
        List<TimeSlotEntity> timeslots = repository.findByOffice(office);
        timeslots.forEach(timeslot -> {
            reservationService.removeTimeSlot(timeslot);
        });

        repository.deleteAll(timeslots);
    }

}
