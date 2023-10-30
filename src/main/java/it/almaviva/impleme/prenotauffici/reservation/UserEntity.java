package it.almaviva.impleme.prenotauffici.reservation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Users", schema = "prenotauffici")
public class UserEntity {


    @Column
    private String name;
    @Column
    private String surname;
    @Id
    private String fiscalCode;
    @Column
    private String email;
    @Column
    private String telNumber;
    @Column
    private String notes;
}
