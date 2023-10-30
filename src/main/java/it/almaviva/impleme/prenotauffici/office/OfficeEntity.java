package it.almaviva.impleme.prenotauffici.office;

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
@Table(name = "Office", schema = "prenotauffici")
public class OfficeEntity {

    @GeneratedValue @Id
    private UUID id;
    
    @Column(name = "municipality_id")
    private String municipalityId;
    @Column
    private String name;
    @Column
    private String  address;
    @Column
    private Double longitude;
    @Column
    private Double latitude;
    @Column
    private String telephoneNumber;
    @Column
    private String site;
    @Column
    private String email;
    @Column
    private String imageUrl;
    @Column
    private String description;
    @Column
    private Status status;


}
