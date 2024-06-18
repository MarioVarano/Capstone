package it.epicode.backend.capstone.prestazione;

import it.epicode.backend.capstone.professionista.Professionista;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prestazioni")
public class Prestazione {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String descrizione;
    @Column(nullable = false)
    private Double prezzo;
    @Column(nullable = false)
    private Boolean attivo = true;


    @ManyToOne
    private Professionista professionista;


}

