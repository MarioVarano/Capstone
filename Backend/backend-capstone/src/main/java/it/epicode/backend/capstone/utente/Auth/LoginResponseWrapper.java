package it.epicode.backend.capstone.utente.Auth;

import it.epicode.backend.capstone.professionista.Auth.LoginResponseProfessionistaDTO;
import lombok.Data;


@Data
public class LoginResponseWrapper {
    private LoginResponseDTO userResponse;
    private  LoginResponseProfessionistaDTO loginResponseProfession;

    public LoginResponseWrapper(LoginResponseDTO userResponse) {
        this.userResponse = userResponse;
    }


    public LoginResponseWrapper(LoginResponseProfessionistaDTO loginResponseProfession) {
        this.loginResponseProfession = loginResponseProfession;
    }
}
