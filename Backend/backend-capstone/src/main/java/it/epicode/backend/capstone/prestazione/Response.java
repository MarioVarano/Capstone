package it.epicode.backend.capstone.prestazione;

import lombok.Data;

@Data
public class Response {
    private Long id;
    private String nome;
    private String descrizione;
    private Double prezzo;
    private Long professionistaId;
    private String professionistaNome;
    private Boolean attivo;
}
