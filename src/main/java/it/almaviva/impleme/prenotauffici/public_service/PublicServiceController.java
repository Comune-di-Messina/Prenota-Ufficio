package it.almaviva.impleme.prenotauffici.public_service;

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

import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceCreationDto;
import it.almaviva.impleme.prenotauffici.public_service.dto.PublicServiceDto;

@RestController
@RequestMapping("v1/publicservice")
@CrossOrigin(origins = "*")
public class PublicServiceController {


    @Autowired
    PublicServiceService service;

    @Autowired
    PublicServiceMapper mapper;

    @GetMapping
    public List<PublicServiceDto> list(@RequestParam Optional<UUID> serviceTypeId, @RequestParam Optional<UUID> officeId, @RequestParam Optional<String> municipalityId){
        return mapper.map(service.getAll(serviceTypeId, officeId));
    }

    @GetMapping("{id}")
    public PublicServiceDto get(@PathVariable UUID id){
        return mapper.map(service.getById(id));
    }

    @PutMapping("{id}")
    public PublicServiceDto update(@PathVariable UUID id, @RequestBody PublicServiceCreationDto dto){
        return mapper.map(service.update(id, dto));
    }

    @PostMapping
    public PublicServiceDto create(@RequestBody PublicServiceCreationDto dto){
        return mapper.map(service.create(dto));
    }

    @DeleteMapping("{id}")
    public PublicServiceDto delete(@PathVariable UUID id){
        return mapper.map(service.delete(id));
    }

    
}
