package it.epicode.backend.capstone.utente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente,Long> {
    List<ResponsePrj> findAllBy();
    Utente findByEmail(String email);





    boolean existsByEmail(String email);

}
