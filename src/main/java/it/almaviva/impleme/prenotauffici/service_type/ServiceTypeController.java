package it.almaviva.impleme.prenotauffici.service_type;

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

import it.almaviva.impleme.prenotauffici.service_type.dto.ServiceTypeCreationDto;
import it.almaviva.impleme.prenotauffici.service_type.dto.ServiceTypeDto;

@RestController
@RequestMapping("v1/serviceTypes")
@CrossOrigin(origins = "*")
public class ServiceTypeController {


    @Autowired
    ServiceTypeService service;

    @Autowired
    ServiceTypeMapper mapper;

    @GetMapping
    public List<ServiceTypeDto> list(@RequestParam Optional<String> municipalityId){
        return mapper.map(service.getAll(municipalityId));
    }

    @GetMapping("{id}")
    public ServiceTypeDto get(@PathVariable UUID id){
        return mapper.map(service.getById(id));
    }

    @PutMapping("{id}")
    public ServiceTypeDto update(@PathVariable UUID id, @RequestBody ServiceTypeCreationDto dto){
        return mapper.map(service.update(id, dto));
    }

    @PostMapping
    public ServiceTypeDto create(@RequestBody ServiceTypeCreationDto dto){
        return mapper.map(service.create(dto));
    }

    @DeleteMapping("{id}")
    public ServiceTypeDto delete(@PathVariable UUID id){
        return mapper.map(service.delete(id));
    }

    
}
