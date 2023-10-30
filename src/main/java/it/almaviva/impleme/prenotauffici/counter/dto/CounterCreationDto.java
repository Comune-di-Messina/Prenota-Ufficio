package it.almaviva.impleme.prenotauffici.counter.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CounterCreationDto {
    private String number;
    private List<UUID> publicServiceId;

}
