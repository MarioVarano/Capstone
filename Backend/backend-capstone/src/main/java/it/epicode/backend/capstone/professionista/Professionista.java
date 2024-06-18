package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.prestazione.Prestazione;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "professionista")
public class Professionista {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String specializzazione;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private String città;

    //@OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL)
    //private List<Prestazione> prestazioni;

    @OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL)
    private List<Appuntamento> appuntamenti;
}

