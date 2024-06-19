package it.epicode.backend.capstone.utente.appuntamentoDTO;

import lombok.Data;

@Data
public class ProfessionistaDTO {
    private Long id;
    private String nome;
    private String cognome;
    private String specializzazione;
}
