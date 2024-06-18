package it.epicode.backend.capstone.appuntamento;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Response {
    private Long id;
    private String oraPrenotazione;
    private LocalDate dataPrenotazione;
}

