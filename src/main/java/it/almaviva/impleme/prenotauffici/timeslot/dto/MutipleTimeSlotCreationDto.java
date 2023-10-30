package it.almaviva.impleme.prenotauffici.timeslot.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MutipleTimeSlotCreationDto {

    @NotNull(message = "startTime can not be null")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone="CET")
    @Schema(type="string" , format = "time", example = "HH:mm")
    private LocalTime startTime;
    
    @NotNull(message = "endTime can not be null")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone="CET")
    @Schema(type="string" , format = "time", example = "HH:mm")
    private LocalTime endTime;
    @NotNull(message = "days can not be null")
    private List<DayOfWeek> days;
    @NotNull(message = "serviceDuration can not be null")
    private Integer serviceDuration;
    @NotNull(message = "contingency can not be null")
    private Integer contingency;
}
