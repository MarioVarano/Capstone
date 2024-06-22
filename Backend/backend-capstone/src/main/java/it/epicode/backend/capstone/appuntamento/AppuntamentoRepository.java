package it.epicode.backend.capstone.appuntamento;

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

    List<Appuntamento> findByUtenteId(Long utenteId);
    List<Appuntamento> findByProfessionistaId(Long professionistaId);




    List<Appuntamento> findByUtenteIdAndDataPrenotazioneAndOraPrenotazione(
            Long utenteId, LocalDate dataPrenotazione, String oraPrenotazione);


}



