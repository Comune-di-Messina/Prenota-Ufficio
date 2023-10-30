package it.almaviva.impleme.prenotauffici.timeslot.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TimeSlotCreationDto {
    @NotNull(message = "dayOfWeek can not be null")
    private DayOfWeek dayOfWeek;
    @NotNull(message = "startTime can not be null")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone="CET")
    @Schema(type="string" , format = "time", example = "HH:mm")
	private LocalTime startTime;
    @NotNull(message = "endTime can not be null")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone="CET")
    @Schema(type="string" , format = "time", example = "HH:mm")
	private LocalTime endTime;
    private LocalDate date;

    @NotNull(message = "reservable can not be null")
    private Boolean reservable;
}
