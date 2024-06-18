package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.ruoli.Ruoli;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Utenti")
public class Utente {
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
    private String telefono;
    @Column(nullable = false)
    private String città;

    @ManyToOne
    @JoinColumn(name = "roleType", nullable = false)
    @Column(nullable = false)
    private Ruoli ruolo;


    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Appuntamento> appuntamenti;

}
