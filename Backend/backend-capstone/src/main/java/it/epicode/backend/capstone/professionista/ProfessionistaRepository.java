package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.utente.ResponsePrj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionistaRepository extends JpaRepository<Professionista,Long> {
    List<ResponsePrj> findAllBy();
}
