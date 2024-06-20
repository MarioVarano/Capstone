package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.base.BaseEntity;
import it.epicode.backend.capstone.ruoli.Ruoli;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "Utenti")
public class Utente extends BaseEntity {


    @Column(nullable = false)
    private String telefono;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "utente_ruolo",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id")
    )
    private final List<Ruoli> ruoli = new ArrayList<>();


    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appuntamento> appuntamenti = new ArrayList<>();



}
