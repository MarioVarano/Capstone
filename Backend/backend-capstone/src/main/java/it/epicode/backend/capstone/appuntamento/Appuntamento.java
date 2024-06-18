package it.epicode.backend.capstone.appuntamento;

import it.epicode.backend.capstone.enums.Stato;
import it.epicode.backend.capstone.prestazione.Prestazione;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.utente.Utente;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
public class Appuntamento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professionista_id", nullable = false)
    private Professionista professionista;
    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
    //@ManyToOne
    //@JoinColumn(name = "prestazione_id")
    //private Prestazione prestazione;

    @Column(nullable = false)
    private Timestamp dataOra;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stato stato;
    private LocalDateTime dataPrenotazione;


    @Column(nullable = false)
    private String messaggio;


    // Verifica se l'appuntamento è in un orario e giorno validi
    @PrePersist
    @PreUpdate
    private void validateAppuntamento() {
        LocalDateTime appointmentDateTime = dataOra.toLocalDateTime();
        DayOfWeek dayOfWeek = appointmentDateTime.getDayOfWeek();
        int hour = appointmentDateTime.getHour();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Gli appuntamenti possono essere presi solo dal lunedì al venerdì.");
        }

        if (hour < 9 || hour >= 17) {
            throw new IllegalArgumentException("Gli appuntamenti possono essere presi solo tra le 9 e le 17.");
        }

        if (!appointmentDateTime.plusHours(1).toLocalDate().equals(appointmentDateTime.toLocalDate())) {
            throw new IllegalArgumentException("Gli appuntamenti devono durare un'ora.");
        }
    }


}
