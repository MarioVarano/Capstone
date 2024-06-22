package it.epicode.backend.capstone.utente.appuntamentoDTO;

import lombok.Data;

@Data
public class UtenteAppuntamentoDTO {
    private Long id; // Id dell'appuntamento
    private String dataPrenotazione;
    private String oraPrenotazione;
    private boolean confermato;
    private ProfessionistaDTO professionista;
}
