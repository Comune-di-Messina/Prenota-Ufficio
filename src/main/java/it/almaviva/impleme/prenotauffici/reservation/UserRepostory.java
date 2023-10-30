package it.almaviva.impleme.prenotauffici.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepostory  extends JpaRepository<UserEntity, String> {
    
}
