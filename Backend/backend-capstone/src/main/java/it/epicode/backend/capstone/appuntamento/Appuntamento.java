package it.epicode.backend.capstone.appuntamento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.utente.User;
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
    @JoinColumn(name = "professionista_id")
    @ToString.Exclude
    @JsonIgnoreProperties("appuntamenti")
    private Professionista professionista;

    @ManyToOne
    @JoinColumn(name = "utente_id")
    @ToString.Exclude
    @JsonIgnoreProperties("appuntamenti")
    private User utente;
    //@ManyToOne
    //@JoinColumn(name = "prestazione_id")
    //private Prestazione prestazione;

    @Column(nullable = false)
    private String oraPrenotazione;
    @Column(nullable = false)
    private boolean confermato = false;
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
