package it.epicode.backend.capstone.utente.Auth;

import it.epicode.backend.capstone.professionista.Auth.LoginResponseProfessionistaDTO;
import lombok.Data;

@Data
public class LoginResponseWrapper {
    private LoginResponseDTO userResponse;

    public LoginResponseWrapper(LoginResponseDTO userResponse) {
        this.userResponse = userResponse;
    }

    public LoginResponseWrapper(LoginResponseProfessionistaDTO professionistaResponse) {
        this.professionistaResponse = professionistaResponse;
    }

    private LoginResponseProfessionistaDTO professionistaResponse;

}
