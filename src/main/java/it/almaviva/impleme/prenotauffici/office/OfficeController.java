package it.almaviva.impleme.prenotauffici.office;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.almaviva.impleme.prenotauffici.office.dto.OfficeCreationDto;
import it.almaviva.impleme.prenotauffici.office.dto.OfficeDto;

@RestController
@RequestMapping("v1/municipality/{municipalityId}/office")
@CrossOrigin(origins = "*")
public class OfficeController {


    @Autowired OfficeService service;
    @Autowired OfficeMapper mapper;

    @GetMapping
    public List<OfficeDto> list(@PathVariable String municipalityId, @RequestParam Optional<UUID> serviceType, @RequestParam Optional<UUID> serviceId){
        return mapper.map(service.getAll(municipalityId, serviceType, serviceId));
    }

    @GetMapping("{id}")
    public OfficeDto get(@PathVariable String municipalityId, @PathVariable UUID id){
        return mapper.map(service.getById(municipalityId, id));
    }

    @PutMapping("{id}")
    public OfficeDto update(@PathVariable String municipalityId, @PathVariable UUID id, @RequestBody OfficeCreationDto dto){
        return mapper.map(service.update(municipalityId, id, dto));
    }

    @PostMapping
    public OfficeDto create(@PathVariable String municipalityId, @RequestBody OfficeCreationDto dto){
        return mapper.map(service.create(municipalityId, dto));
    }

    @DeleteMapping("{id}")
    public OfficeDto delete(@PathVariable String municipalityId, @PathVariable UUID id){
        return mapper.map(service.delete(municipalityId, id));
    }
    
}
