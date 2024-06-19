package it.epicode.backend.capstone.professionista.appuntamentoDTO;

import it.epicode.backend.capstone.enums.Stato;
import lombok.Data;

@Data
public class ProfessionistaAppuntamentoDTO {
    private Long id; // Id dell'appuntamento
    private String dataPrenotazione;
    private String oraPrenotazione;
    private Stato stato;
    private UtenteDTO utente;
}
