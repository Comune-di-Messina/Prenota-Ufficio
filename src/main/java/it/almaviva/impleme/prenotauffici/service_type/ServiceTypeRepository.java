package it.almaviva.impleme.prenotauffici.service_type;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.impleme.prenotauffici.common.Status;

public interface ServiceTypeRepository extends JpaRepository<ServiceTypeEntity, UUID> {
    List<ServiceTypeEntity> findByStatus(Status status);
}
