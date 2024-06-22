package it.epicode.backend.capstone.professionista.Auth;

import it.epicode.backend.capstone.ruoli.Ruoli;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisteredProfessionistaDTO {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String città;
    String specializzazione;
    private List<Ruoli> roles;

    @Builder(setterPrefix = "with")
    public RegisteredProfessionistaDTO(Long id, String firstName, String lastName, String username, String email, String città,String specializzazione, List<Ruoli> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.città = città;
        this.specializzazione = specializzazione;
        this.roles = roles;
    }
}