package it.almaviva.impleme.prenotauffici.public_service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.impleme.prenotauffici.common.Status;
import it.almaviva.impleme.prenotauffici.service_type.ServiceTypeEntity;

public interface PublicServiceRepository extends JpaRepository<PublicServiceEntity, UUID> {

    List<PublicServiceEntity> findByTypeAndStatus(ServiceTypeEntity type, Status status);
    List<PublicServiceEntity> findByStatus(Status status);
    
}
