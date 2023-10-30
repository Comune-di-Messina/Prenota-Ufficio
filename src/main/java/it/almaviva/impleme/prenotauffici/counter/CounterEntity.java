package it.almaviva.impleme.prenotauffici.counter;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;
import it.almaviva.impleme.prenotauffici.public_service.PublicServiceEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "Counter", schema = "prenotauffici")
public class CounterEntity {
    
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office", nullable = false, insertable = true, updatable = false)
    private OfficeEntity office;

    @Column
    private String number;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "avaiable_services", joinColumns = @JoinColumn(name = "counter_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<PublicServiceEntity> publicService;
}
