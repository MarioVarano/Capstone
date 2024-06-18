package it.epicode.backend.capstone.utente;


import lombok.Data;

@Data
public class Response {
    private Long id;
    private String nome;
    private String cognome;
    private String email;
}
