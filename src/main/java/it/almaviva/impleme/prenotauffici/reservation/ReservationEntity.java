package it.almaviva.impleme.prenotauffici.reservation;


import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceEntity;
import it.almaviva.impleme.prenotauffici.timeslot.TimeSlotEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "Reservations", schema = "prenotauffici")
public class ReservationEntity {
    

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslot",  nullable = true) 
    private TimeSlotEntity timeslot;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office",  nullable = true) 
    private OfficeEntity office;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", nullable = false) 
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service",  nullable = true) 
    private PublicServiceEntity publicService;
    
    @Column
    private LocalDate date;

    @Column
    private ReservationStatus status;

    @Column
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] qrcode;

    @Column
    private String notes;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;
}
