package it.almaviva.impleme.prenotauffici.timeslot;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.almaviva.impleme.prenotauffici.timeslot.dto.MutipleTimeSlotCreationDto;
import it.almaviva.impleme.prenotauffici.timeslot.dto.TimeSlotCreationDto;
import it.almaviva.impleme.prenotauffici.timeslot.dto.TimeSlotDto;
import it.almaviva.impleme.prenotauffici.timeslot.exception.TimeslotConflict;

@RestController
@RequestMapping("v1/municipality/{municipalityId}/office/{officeId}/timeslots")
public class TimeSlotController {

    @Autowired
    TimeSlotMapper mapper;

    @Autowired
    TimeslotService service;

    @GetMapping
    public List<TimeSlotDto> list(@PathVariable String municipalityId, @PathVariable UUID officeId) {
        return mapper.map(service.getAll(municipalityId, officeId));
    }

    @GetMapping("{id}")
    public TimeSlotDto get(@PathVariable String municipalityId, @PathVariable UUID officeId, @PathVariable UUID id) {
        return mapper.map(service.getById(municipalityId, officeId, id));
    }

    @PatchMapping(value = "{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Two or more TimeSlots are placed in the same time", content =
            { @Content(mediaType = "application/json", schema = @Schema(implementation = TimeslotConflict.class)) }),

            @ApiResponse(responseCode = "200", description = "TimeSlot updated", content =
            { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TimeSlotDto.class)) }) 
    })

    public TimeSlotDto update(@PathVariable String municipalityId, @PathVariable UUID officeId, @PathVariable UUID id,
            @RequestBody @Valid TimeSlotCreationDto dto) throws TimeslotConflict {
       
        return mapper.map(service.update(municipalityId, officeId, id, dto));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Two or more TimeSlots are placed in the same time", content =
            { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TimeslotConflict.class)) }),

            @ApiResponse(responseCode = "201", description = "New TimeSlot created", content =
            { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TimeSlotDto.class)) }) 
    })
    @ResponseStatus(HttpStatus.CREATED)
    public TimeSlotDto create(@PathVariable String municipalityId, @PathVariable UUID officeId, @RequestBody @Valid TimeSlotCreationDto dto) throws TimeslotConflict {
        return mapper.map(service.create(municipalityId, officeId, dto));
    }

    @DeleteMapping("{id}")
    public TimeSlotDto delete(@PathVariable String municipalityId, @PathVariable UUID officeId, @PathVariable UUID id) {
        return mapper.map(service.delete(municipalityId, officeId, id));
    }

    @PostMapping(value="multiple", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "Two or more TimeSlots are placed in the same time", content =
            { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TimeslotConflict.class)) }),

            @ApiResponse(responseCode = "201", description = "New TimeSlots created", content =
            { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema (implementation = TimeSlotDto.class) )) }) 
    })
    @ResponseStatus(HttpStatus.CREATED)
    public List<TimeSlotDto> createMutliple(@PathVariable String municipalityId, @PathVariable UUID officeId, @RequestBody MutipleTimeSlotCreationDto dto) throws TimeslotConflict {
        return mapper.map(service.create(municipalityId, officeId, dto));
    }

}
