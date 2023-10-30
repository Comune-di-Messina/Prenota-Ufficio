package it.almaviva.impleme.prenotauffici.reservation.dto;


import lombok.Data;

@Data
public class ReservationUserCreationDto {
    private String name;
    private String surname;
    private String fiscalCode;
    private String email;
    private String telNumber;
}
