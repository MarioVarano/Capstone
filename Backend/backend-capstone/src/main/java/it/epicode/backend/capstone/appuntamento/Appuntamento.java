package it.epicode.backend.capstone.appuntamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.backend.capstone.enums.Stato;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.utente.Utente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "appuntamenti")
public class Appuntamento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnoreProperties("appuntamenti")
    private Professionista professionista;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnoreProperties("appuntamenti")
    private Utente utente;
    //@ManyToOne
    //@JoinColumn(name = "prestazione_id")
    //private Prestazione prestazione;

    @Column(nullable = false)
    private String oraPrenotazione;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stato stato;
    private LocalDate dataPrenotazione;


    /*
   @Column(nullable = false)
    private String messaggio;
   */


    @PrePersist
    @PreUpdate
    void validateAppuntamento() {
        LocalDateTime appointmentDateTime = LocalDateTime.of(dataPrenotazione, LocalTime.parse(oraPrenotazione));
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
