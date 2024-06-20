package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.base.BaseEntity;
import it.epicode.backend.capstone.prestazione.Prestazione;
import it.epicode.backend.capstone.ruoli.Ruoli;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "professionista")
public class Professionista extends BaseEntity {


    @Column(nullable = false)
    private String specializzazione;

    @Column(nullable = false)
    private String descrizione;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "professionista_ruolo",
            joinColumns = @JoinColumn(name = "professionista_id"),
            inverseJoinColumns = @JoinColumn(name = "ruolo_id")
    )
    private final List<Ruoli> ruoli = new ArrayList<>();


    //@OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL)
    //private List<Prestazione> prestazioni;

    @OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appuntamento> appuntamenti = new ArrayList<>();
}

