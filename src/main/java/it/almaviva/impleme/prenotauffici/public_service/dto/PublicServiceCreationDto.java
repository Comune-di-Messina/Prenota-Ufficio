package it.almaviva.impleme.prenotauffici.public_service.dto;

import java.util.UUID;

import it.almaviva.impleme.prenotauffici.public_service.ReservationType;
import lombok.Data;

@Data
public class PublicServiceCreationDto {
    private String name;
    private String description;
    private String notes;
    private UUID typeId;
    private String labelField;
    private Boolean mandatoryField;
    private String fieldNotes;
    private ReservationType reservationType;
}
