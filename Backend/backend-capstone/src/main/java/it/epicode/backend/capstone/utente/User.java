package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.ruoli.Ruoli;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String firstName;
    @Column(length = 50, nullable = false)
    private String lastName;
    @Column(length = 50, nullable = false)
    private String username;
    @Email
    private String email;
    @Column(length = 125, nullable = false)
    private String password;
    private String avatar;
    @Column(length = 50, nullable = false)
    private String città;
    @ManyToMany(fetch = FetchType.EAGER)
    private final List<Ruoli> roles = new ArrayList<>();
}





