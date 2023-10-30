package it.almaviva.impleme.prenotauffici.public_service;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.almaviva.impleme.prenotauffici.common.Status;
import it.almaviva.impleme.prenotauffici.service_type.ServiceTypeEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "Services", schema = "prenotauffici")
public class PublicServiceEntity {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional=false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "type")
    private ServiceTypeEntity type;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String notes;

    @Column
    private String labelField;

    @Column
    private Boolean mandatoryField;

    @Column
    private String fieldNotes;

    @Column
    private ReservationType reservationType;

    @Column
    private Status status;
}
