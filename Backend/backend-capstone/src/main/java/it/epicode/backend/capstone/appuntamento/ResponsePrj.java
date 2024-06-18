package it.epicode.backend.capstone.appuntamento;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

public interface ResponsePrj {
    @Value("#{target.utente.nome} #{target.utente.cognome}")
    String getNomeUtente();

    @Value("#{target.professionista.nome} #{target.professionista.cognome}")
    String getNomeProfessionista();

    Timestamp getDataOra();
}
