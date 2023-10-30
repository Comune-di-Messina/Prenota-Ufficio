package it.almaviva.impleme.prenotauffici.reservation;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceEntity;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;

public interface ReservationRepository  extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByOffice(OfficeEntity office);

    List<ReservationEntity> findByPublicService(PublicServiceEntity publicService);

    List<ReservationEntity> findByTimeslot(TimeSlotEntity timeslot);
    
    List<ReservationEntity> findByTimeslotAndDate(TimeSlotEntity timeslot,LocalDate date);
    List<ReservationEntity> findByTimeslotAndDateAndPublicService(TimeSlotEntity timeslot,LocalDate date, PublicServiceEntity publicService);
    List<ReservationEntity> findByOfficeAndPublicServiceIn(OfficeEntity office, Collection<PublicServiceEntity> publicService);    
    List<ReservationEntity> findByDate(LocalDate date);
    List<ReservationEntity> findByUser(UserEntity user);
    

    

}
