package it.epicode.backend.capstone.utente;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Request {
    @NotEmpty(message = "Il nome non puo' essere vuoto")
    private String nome;
    @NotEmpty(message = "Il cognome non puo' essere vuoto")
    private String cognome;
    @NotEmpty(message = "La mail non puo' essere vuota")
    private String email;
    @NotEmpty(message = "La password non puo' essere vuota")
    private String password;
    @NotEmpty(message = "Il ruolo non puo' essere vuoto")
    private String ruolo;
}
