package it.epicode.backend.capstone.professionista.Auth;

import it.epicode.backend.capstone.utente.Auth.RegisteredUserDTO;
import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseProfessionistaDTO {
    RegisteredProfessionistaDTO professionista;
    String specializzazione;
    String token;

    @Builder(setterPrefix = "with")
    public LoginResponseProfessionistaDTO(RegisteredProfessionistaDTO professionista, String token,String specializzazione) {
        this.professionista = professionista;
        this.token = token;
        this.specializzazione = specializzazione;
    }
}

