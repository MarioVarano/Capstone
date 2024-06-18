package it.epicode.backend.capstone.prestazione;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Request {
    @NotEmpty(message = "Il nome non puo' essere vuoto")
    private String nome;
    @NotEmpty(message = "La descrizione non puo' essere vuota")
    private String descrizione;
    @NotEmpty(message = "Il prezzo non puo' essere vuoto")
    private Double prezzo;
    @NotEmpty(message = "Id professionista deve esserci")
    private Long professionistaId;
    @NotEmpty(message = "Lo stato della prestazione non puo' essere vuoto")
    private Boolean attivo;
}
