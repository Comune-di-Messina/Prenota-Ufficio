package it.almaviva.impleme.prenotauffici.office.dto;

import java.util.UUID;

import it.almaviva.impleme.prenotauffici.office.Coordinates;
import lombok.Data;

@Data
public class OfficeDto {
    private UUID id;
    private String municipalityId;
    private String name;
    private String description;
    private Coordinates coordinates; 
    private String address; 
    private String telephoneNumber;
    private String site;
    private String email;
    private String imageUrl;

}
