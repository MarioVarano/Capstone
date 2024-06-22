package it.epicode.backend.capstone.professionista.appuntamentoDTO;

import lombok.Data;

@Data
public class ProfessionistaAppuntamentoDTO {
    private Long id; // Id dell'appuntamento
    private String dataPrenotazione;
    private String oraPrenotazione;
    private boolean confermato;
    private UtenteDTO utente;
}
