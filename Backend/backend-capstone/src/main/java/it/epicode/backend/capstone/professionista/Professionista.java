package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.utente.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "professionista")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Professionista extends User {


    @Column(nullable = false)
    private String specializzazione;

    @Column(nullable = false)
    private String descrizione;



    //@OneToMany(mappedBy = "professionista", cascade = CascadeType.ALL)
    //private List<Prestazione> prestazioni;

}

