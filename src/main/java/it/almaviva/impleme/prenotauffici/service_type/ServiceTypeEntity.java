package it.almaviva.impleme.prenotauffici.service_type;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import it.almaviva.impleme.prenotauffici.common.Status;
import lombok.Data;
@Data
@Entity
@Table(name = "Services_type", schema = "prenotauffici")
public class ServiceTypeEntity {

     @GeneratedValue @Id
     private UUID id;

     @Column
     private String name;

     @Column
     private Status status;
    
}
