package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.professionista.Professionista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtenteRepository extends JpaRepository<Utente,Long> {
    List<ResponsePrj> findAllBy();
    Utente findByEmail(String email);
}
