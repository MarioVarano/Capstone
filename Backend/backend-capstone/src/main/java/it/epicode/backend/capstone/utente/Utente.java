package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.base.BaseEntity;
import it.epicode.backend.capstone.ruoli.Ruoli;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "Utenti")
public class Utente extends BaseEntity {


    @Column(nullable = false)
    private String telefono;

    //@ManyToOne
    //@JoinColumn(name = "ruolo_id", nullable = false)
    //private Ruoli ruolo;


    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Appuntamento> appuntamenti;

}
