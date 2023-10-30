package it.almaviva.impleme.prenotauffici.timeslot.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import it.almaviva.impleme.prenotauffici.office.dto.OfficeDto;
import lombok.Data;

@Data
public class TimeSlotDto {
    private UUID id;
    private OfficeDto office;
    private DayOfWeek dayOfWeek;
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone="CET")
    @Schema(type="string" , format = "time", example = "HH:mm")
	private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone="CET")
    @Schema(type="string" , format = "time", example = "HH:mm")
	private LocalTime endTime;
    private LocalDate date;
    private Boolean reservable;
    
}
