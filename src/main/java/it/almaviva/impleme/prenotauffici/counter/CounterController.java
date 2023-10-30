package it.almaviva.impleme.prenotauffici.counter;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.impleme.prenotauffici.counter.dto.CounterCreationDto;
import it.almaviva.impleme.prenotauffici.counter.dto.CounterDto;

@RestController
@RequestMapping("v1/municipality/{municipalityId}/office/{officeId}/counter")
@CrossOrigin(origins = "*")
public class CounterController {

    @Autowired
    CounterService service;

    @Autowired
    CounterMapper mapper;

    @GetMapping
    public List<CounterDto> list(@PathVariable String municipalityId, @PathVariable UUID officeId){
        return mapper.map(service.getAll(municipalityId, officeId));
    }

    @GetMapping("{id}")
    public CounterDto get(@PathVariable String municipalityId, @PathVariable UUID officeId,@PathVariable UUID id){
        return mapper.map(service.getById(municipalityId, officeId, id));
    }

    @PatchMapping("{id}")
    public CounterDto update(@PathVariable String municipalityId, @PathVariable UUID officeId, @PathVariable UUID id, @RequestBody CounterCreationDto dto){
        return mapper.map(service.update(municipalityId, officeId, id, dto));
    }

    @PostMapping
    public CounterDto create(@PathVariable String municipalityId, @PathVariable UUID officeId, @RequestBody CounterCreationDto dto){
        return mapper.map(service.create(municipalityId, officeId, dto));
    }

    @DeleteMapping("{id}")
    public CounterDto delete(@PathVariable String municipalityId, @PathVariable UUID officeId,@PathVariable UUID id){
        return mapper.map(service.delete(municipalityId, officeId, id));
    }
    
}
