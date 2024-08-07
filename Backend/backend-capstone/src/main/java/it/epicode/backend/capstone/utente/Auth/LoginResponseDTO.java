package it.epicode.backend.capstone.utente.Auth;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDTO {
    RegisteredUserDTO user;
    String specializzazione;
    String token;

    @Builder(setterPrefix = "with")
    public LoginResponseDTO(RegisteredUserDTO user,String token, String specializzazione) {
        this.user = user;
        this.token = token;
        this.specializzazione = specializzazione;
    }
}

