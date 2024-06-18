package it.epicode.backend.capstone.professionista;

import lombok.Data;

@Data
public class Response {
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String specializzazione;
    private String descrizione;
    private String indirizzoStudio;
}
