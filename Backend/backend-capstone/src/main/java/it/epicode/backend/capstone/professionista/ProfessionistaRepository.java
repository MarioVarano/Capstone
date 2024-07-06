package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.utente.ResponsePrj;
import it.epicode.backend.capstone.utente.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionistaRepository extends JpaRepository<Professionista,Long> {
    List<ResponsePrj> findAllBy();

    boolean existsByUsername(String username);

    List<Professionista> findByCity(String city);
    List<Professionista> findBySpecializzazione(String specializzazione);


    boolean existsByEmail(String email);

}
