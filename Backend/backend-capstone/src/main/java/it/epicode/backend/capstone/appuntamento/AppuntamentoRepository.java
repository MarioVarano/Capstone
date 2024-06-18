package it.epicode.backend.capstone.appuntamento;

import it.epicode.backend.capstone.utente.ResponsePrj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento,Long> {

    @Query("SELECT a FROM Appuntamento a")
    List<ResponsePrj> findAllBy();


    List<Appuntamento> findByProfessionistaIdAndDataPrenotazioneAndOraPrenotazione(
            Long id, LocalDate dataPrenotazione, String oraPrenotazione);
}


