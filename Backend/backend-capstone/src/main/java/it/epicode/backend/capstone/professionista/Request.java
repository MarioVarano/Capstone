package it.epicode.backend.capstone.professionista;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Request {
    @NotEmpty(message = "Il nome non puo' essere vuoto")
    private String nome;
    @NotEmpty(message = "Il cognome non puo' essere vuoto")
    private String cognome;
    @NotEmpty(message = "La mail non puo' essere vuoto")
    @Email
    private String email;
    @NotEmpty(message = "La password non puo' essere vuota")
    private String password;
    @NotEmpty(message = "La specializzazione non puo' essere vuoto")
    private String specializzazione;
    @NotEmpty(message = "La descrizione non puo' essere vuoto")
    private String descrizione;
    @NotEmpty(message = "La città non puo' essere vuoto")
    private String città;
    @NotEmpty(message = "L'avatar non puo' essere vuoto")
    private String avatar;

}
