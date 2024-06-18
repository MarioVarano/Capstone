package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.base.BaseEntity;
import it.epicode.backend.capstone.prestazione.Prestazione;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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



    //@OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL)
    //private List<Prestazione> prestazioni;

    @OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL)
    private List<Appuntamento> appuntamenti;
}

