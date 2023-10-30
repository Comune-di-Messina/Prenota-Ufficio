package it.almaviva.impleme.prenotauffici.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.impleme.prenotauffici.reservation.ReservationMapper;
import it.almaviva.impleme.prenotauffici.reservation.ReservationService;
import it.almaviva.impleme.prenotauffici.reservation.dto.ReservationDto;

@RestController
@RequestMapping("v1/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    ReservationService service;

    @Autowired
    ReservationMapper mapper;

    @GetMapping("{id}/reservations")
    public List<ReservationDto> reservationsList(@PathVariable String id, @RequestParam Optional<String> municipalityId,  @RequestParam Optional<UUID> officeId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate){
        return mapper.map(service.getAll(Optional.of(id), municipalityId, officeId, startDate, endDate));
        
    }
    
}
