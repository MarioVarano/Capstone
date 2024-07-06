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
    String city;
    String avatar;
    private List<Ruoli> roles;

    @Builder(setterPrefix = "with")
    public RegisteredUserDTO(Long id, String firstName, String lastName, String username, String email,String city,String avatar, List<Ruoli> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.city = city;
        this.avatar = avatar;
        this.roles = roles;
    }
}