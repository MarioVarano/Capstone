package it.epicode.backend.capstone.appuntamento;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.LocalDate;

public interface ResponsePrj {
    Long getId();

    @Value("#{target.utente.nome + ' ' + target.utente.cognome}")
    String getNomeCompletoUtente();

    @Value("#{target.professionista.nome + ' ' + target.professionista.cognome}")
    String getNomeCompletoProfessionista();

    String getOraPrenotazione();

    LocalDate getDataPrenotazione();
}
