package it.almaviva.impleme.prenotauffici.office;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import it.almaviva.impleme.prenotauffici.common.Status;

public interface OfficeRepository extends JpaRepository<OfficeEntity, UUID> {
    

    List<OfficeEntity> findByMunicipalityId(String municipalityId);
    List<OfficeEntity> findByMunicipalityIdAndStatus(String municipalityId, Status status);
}
