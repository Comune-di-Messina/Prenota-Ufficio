package it.almaviva.impleme.prenotauffici.timeslot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "TimeSlot", schema = "prenotauffici")
public class TimeSlotEntity {
    @Id @GeneratedValue
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office", nullable = false, insertable = true, updatable = false)
    private OfficeEntity office;
    
    @Column
    private DayOfWeek dayOfWeek;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;
    
    @Column
    private LocalDate date;

    @Column
    private Boolean reservable;
}
