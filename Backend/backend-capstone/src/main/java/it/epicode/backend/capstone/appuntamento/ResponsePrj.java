package it.epicode.backend.capstone.appuntamento;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;

public interface ResponsePrj {
    @Value("#{target.utente.nome}") // l'annotazione Value recupera la posizione dell'elemento richiesto all'interno della classe
    String getNomeUtente();
    @Value("#{target.professionista.nome}") // l'annotazione Value recupera la posizione dell'elemento richiesto all'interno della classe
    String getNomeProfessionista();
    Timestamp getDataOra();
}
