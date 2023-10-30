package it.almaviva.impleme.prenotauffici.counter.dto;

import java.util.List;
import java.util.UUID;

import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceDto;
import lombok.Data;

@Data
public class CounterDto {
    private UUID id;
    private String number;
    private UUID officeId;
    private List<PublicServiceDto> publicService;
}
