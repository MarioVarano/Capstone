package it.epicode.backend.capstone.appuntamento;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {
    private Long id;
    private LocalDateTime orarioInizio;
    private LocalDateTime orarioFine;
    private LocalDateTime dataPrenotazione;
}
