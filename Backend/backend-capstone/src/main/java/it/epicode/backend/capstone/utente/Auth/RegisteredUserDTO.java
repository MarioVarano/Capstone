package it.epicode.backend.capstone.utente.Auth;

import it.epicode.backend.capstone.ruoli.Ruoli;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RegisteredUserDTO {
    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String città;
    private List<Ruoli> roles;

    @Builder(setterPrefix = "with")
    public RegisteredUserDTO(Long id, String firstName, String lastName, String username, String email,String città, List<Ruoli> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.città = città;
        this.roles = roles;
    }
}