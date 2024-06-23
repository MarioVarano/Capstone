package it.epicode.backend.capstone.utente.Auth;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponseDTO {
    String token;

    @Builder(setterPrefix = "with")
    public LoginResponseDTO(String token) {
        this.token = token;
    }
}

