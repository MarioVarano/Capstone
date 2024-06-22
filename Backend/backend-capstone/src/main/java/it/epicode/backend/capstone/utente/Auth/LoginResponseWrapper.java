package it.epicode.backend.capstone.utente.Auth;

import it.epicode.backend.capstone.professionista.Auth.LoginResponseProfessionistaDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class LoginResponseWrapper {
    private Object response;

    public LoginResponseWrapper(Object response) {
        this.response = response;
    }

}
