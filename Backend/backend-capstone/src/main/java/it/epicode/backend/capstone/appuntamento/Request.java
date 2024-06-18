package it.epicode.backend.capstone.appuntamento;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class Request {
    @NotEmpty(message = "Id professionista deve esserci")
    private Long idProfessionista;
    @NotEmpty(message = "Id utente deve esserci")
    private Long idUtente;
    @NotEmpty(message = "Orario necessario")
    private Timestamp dataOra;
    @NotEmpty(message = "Data necessaria")
    private LocalDateTime dataPrenotazione;

}