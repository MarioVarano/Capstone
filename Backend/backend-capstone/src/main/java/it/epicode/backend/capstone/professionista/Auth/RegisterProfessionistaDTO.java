package it.epicode.backend.capstone.professionista.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterProfessionistaDTO {
    String firstName;
    String lastName;
    String username;
    String email;
    String password;
    String avatar;
    String città;
    String specializzazione;
    String descrizione;
}