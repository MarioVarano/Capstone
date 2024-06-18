package it.epicode.backend.capstone.appuntamento;

import it.epicode.backend.capstone.utente.ResponsePrj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento,Long> {
    List<Appuntamento> findByProfessionistaIdAndDataOraBetween(Long id, Timestamp timestamp, Timestamp timestamp1);
    List<ResponsePrj> findAllBy();
}
