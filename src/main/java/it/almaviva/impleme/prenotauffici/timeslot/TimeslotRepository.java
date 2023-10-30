package it.almaviva.impleme.prenotauffici.timeslot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;

public interface TimeslotRepository extends JpaRepository<TimeSlotEntity, UUID>{
    
    List<TimeSlotEntity> findByOffice(OfficeEntity office);
    List<TimeSlotEntity> findByDayOfWeekAndOfficeAndDate(DayOfWeek dayOfWeek, OfficeEntity office,LocalDate date);

}
