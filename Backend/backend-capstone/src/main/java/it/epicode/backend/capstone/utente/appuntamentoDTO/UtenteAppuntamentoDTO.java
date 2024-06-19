package it.epicode.backend.capstone.utente.appuntamentoDTO;

import it.epicode.backend.capstone.enums.Stato;
import lombok.Data;

@Data
public class UtenteAppuntamentoDTO {
    private Long id; // Id dell'appuntamento
    private String dataPrenotazione;
    private String oraPrenotazione;
    private Stato stato;
    private ProfessionistaDTO professionista;
}
