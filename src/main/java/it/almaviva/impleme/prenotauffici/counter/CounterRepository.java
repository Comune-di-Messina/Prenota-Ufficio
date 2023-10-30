package it.almaviva.impleme.prenotauffici.counter;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.impleme.prenotauffici.office.OfficeEntity;

public interface CounterRepository extends JpaRepository<CounterEntity, UUID> {


    public List<CounterEntity> findByOffice(OfficeEntity office);

    public List<CounterEntity> findByPublicService_Id(UUID publicServiceId);
    public List<CounterEntity> findByPublicService_IdAndOffice(UUID publicServiceId, OfficeEntity office);
    
    
}
