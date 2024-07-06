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
    String city;
    String avatar;
    String specializzazione;
    String descrizione;
    private List<Ruoli> roles;

    @Builder(setterPrefix = "with")
    public RegisteredProfessionistaDTO(Long id, String firstName, String lastName, String username, String email, String city,String avatar,String specializzazione, String descrizione, List<Ruoli> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.city = city;
        this.avatar = avatar;
        this.specializzazione = specializzazione;
        this.descrizione = descrizione;
        this.roles = roles;
    }
}