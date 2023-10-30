package it.almaviva.impleme.prenotauffici.reservation;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.auth0.jwt.interfaces.Claim;
import it.almaviva.impleme.prenotauffici.reservation.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.almaviva.impleme.prenotauffici.calendar.CalendarService;
import it.almaviva.impleme.prenotauffici.counter.CounterEntity;
import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import it.almaviva.impleme.prenotauffici.office.OfficeService;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceEntity;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceService;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationCreationDto;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationUpdateDto;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;
import it.almaviva.impleme.prenotauffici.timeslot.TimeslotService;

@Service
public class ReservationService {

    Logger logger = LoggerFactory.getLogger(ReservationService.class);


    @Autowired
    ReservationMapper mapper;

    @Autowired
    ReservationRepository repository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepostory userRepo;

    @Autowired
    OfficeService officeService;

    @Autowired
    TimeslotService timeslotService;

    @Autowired
    PublicServiceService publicServiceService;

    @Autowired
    CalendarService calendarService;

    @Autowired
    NotifierService notifierService;

    @Transactional
    public ReservationEntity getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ReservationNotFound(id));
    }

    @Transactional
    public List<ReservationEntity>  getAll(Optional<String> fiscalCode, Optional<String> municipalityId, Optional<UUID> officeId,
            Optional<LocalDate> startDate, Optional<LocalDate> endDate) {

        List<ReservationEntity> list;
        Stream<ReservationEntity> stream;
        if (fiscalCode.isPresent()) {
            UserEntity user = userRepo.findById(fiscalCode.get()).orElseThrow(() -> new UserNotFound(fiscalCode.get()));
            stream = repository.findByUser(user).stream();
        } else {
            stream = repository.findAll().stream();
        }

        Map<Boolean, List<ReservationEntity>> splitted = stream.filter(r -> {
            // municipality filter
            if (!municipalityId.map(id -> r.getOffice().getMunicipalityId().equals(id)).orElse(true))
                return false;
            // office filter
            if (!officeId.map(id -> r.getOffice().getId().equals(id)).orElse(true))
                return false;
            // Dates filters
            if (!startDate.map(date -> r.getDate().equals(date) || r.getDate().isAfter(date)).orElse(true))
                return false;
            if (!endDate.map(date -> r.getDate().equals(date) || r.getDate().isBefore(date)).orElse(true))
                return false;
            return true;
        }).sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                .collect(Collectors.partitioningBy(
                        r -> r.getDate().isAfter(LocalDate.now()) && (r.getStatus().equals(ReservationStatus.RICHIESTA)
                                || r.getStatus().equals(ReservationStatus.CONFIRMED))));

        // Sort canceled, revoked and old reservations to the bottom
        list = splitted.getOrDefault(true, new ArrayList<>());
        list.addAll(splitted.getOrDefault(false, new ArrayList<>()));

        return list;
    }

    @Transactional
    public List<ReservationEntity> getByTimeslot(TimeSlotEntity timeslot) {
        return repository.findByTimeslot(timeslot);
    }

    @Transactional
    public List<ReservationEntity> getByTimeslotAndDate(TimeSlotEntity timeslot, LocalDate date) {
        return repository.findByTimeslotAndDate(timeslot, date);
    }

    @Transactional
    public List<ReservationEntity> getByTimeslotAndDateAndPubliService(TimeSlotEntity timeslot, LocalDate date, PublicServiceEntity publicService) {
        return repository.findByTimeslotAndDateAndPublicService(timeslot, date, publicService);
    }

    @Transactional
    public List<ReservationEntity> removeTimeSlot(TimeSlotEntity timeslot) {
        List<ReservationEntity> toUpdate = repository.findByTimeslot(timeslot).stream().map(r ->{ 
            r.setTimeslot(null);
            return r;
        }).collect(Collectors.toList());

        checkFutureReservations(toUpdate);

        return repository.saveAll(toUpdate);
    }

    @Transactional
    public void checkFutureReservationsByService (PublicServiceEntity pService){
        List<ReservationEntity> toUpdate = repository.findByPublicService(pService).stream().collect(Collectors.toList());

        checkFutureReservations(toUpdate);
    }

    @Transactional
    public void checkFutureReservationsByOffice (OfficeEntity office){
        List<ReservationEntity> toUpdate = repository.findByOffice(office);
        checkFutureReservations(toUpdate);
    }

    @Transactional
    public Stream<ReservationEntity> getFutureReservationsByCounter (CounterEntity counter){
        return repository.findByOfficeAndPublicServiceIn(counter.getOffice(), counter.getPublicService()).stream()
        .filter(r -> r.getDate().isAfter(LocalDate.now()) || r.getDate().isEqual(LocalDate.now()))
        .filter( r -> ReservationStatus.CONFIRMED.equals( r.getStatus()) || ReservationStatus.RICHIESTA.equals(r.getStatus()) );
    }

    private void checkFutureReservations(List<ReservationEntity> reservations) {
        reservations.stream()
        .filter(r -> r.getDate().isAfter(LocalDate.now()) || r.getDate().isEqual(LocalDate.now()))
        .filter( r -> ReservationStatus.CONFIRMED.equals( r.getStatus()) || ReservationStatus.RICHIESTA.equals(r.getStatus()) )
        .findAny()
        .ifPresent(r -> { throw new FutureReservationConflict();});
    }

    @Transactional
    public ReservationEntity create(ReservationCreationDto dto) {

        final UserEntity userToSave = userMapper.map(dto.getUser());
        UserEntity user = userRepo.findById(userToSave.getFiscalCode()).orElseGet(() -> userRepo.save(userToSave));



        OfficeEntity office = officeService.getById(dto.getMunicipalityId(), dto.getOfficeId());
        TimeSlotEntity timeSlot = timeslotService.getById(dto.getMunicipalityId(), dto.getOfficeId(),
                dto.getTimeslotId());



        if(LocalDateTime.now(ZoneId.of("Europe/Berlin")).isAfter(LocalDateTime.of(dto.getDate(), timeSlot.getStartTime()))){
        throw new BeforeReservationConflict();
      }

      PublicServiceEntity publicService = publicServiceService.getById(dto.getPublicServiceId());

        if (!calendarService.hasFreeCounter(dto.getMunicipalityId(), dto.getOfficeId(), publicService,
                dto.getDate(), timeSlot)) {
            throw new ReservationFull();
        }

        userRepo.save(user);
        userRepo.flush();

        ReservationEntity reservationToCreate = mapper.map(dto);
        reservationToCreate.setOffice(office);
        reservationToCreate.setTimeslot(timeSlot);
        reservationToCreate.setPublicService(publicService);
        reservationToCreate.setUser(user);
        reservationToCreate.setStatus(ReservationStatus.CONFIRMED);
        reservationToCreate.setStartTime(timeSlot.getStartTime());
        reservationToCreate.setEndTime(timeSlot.getEndTime());

        ReservationEntity reservationToUpdate = repository.save(reservationToCreate);

        byte[] base64Image = notifierService.generateQrcode(reservationToUpdate);
        reservationToUpdate.setQrcode(base64Image);
        ReservationEntity a = repository.save(reservationToUpdate);
        repository.flush();
        notifierService.sendUserNotificatore(user.getFiscalCode(),user.getEmail());
        notifierService.inviaNotifica(a.getId());
        return a;
    }

    @Transactional
    public ReservationEntity update(Long id, ReservationUpdateDto dto) {

        ReservationEntity oldReservation = getById(id);
        ReservationEntity reservationToUpdate = mapper.update(oldReservation, dto);

        final ReservationEntity save = repository.save(reservationToUpdate);
        notifierService.inviaNotifica(save.getId());
        return save;
    }

    @Transactional
    public List<ReservationEntity> getFutureActiveReservationOnTimeslot(TimeSlotEntity timeslot){
        
        return repository.findByTimeslot(timeslot).stream()
        // is today or in the future
        .filter( r -> r.getDate().isAfter(LocalDate.now()) || r.getDate().isEqual(LocalDate.now()))
        // is active 
        .filter( r -> ReservationStatus.CONFIRMED.equals( r.getStatus()) || ReservationStatus.RICHIESTA.equals(r.getStatus()) )
        .collect(Collectors.toList());
    }


}
