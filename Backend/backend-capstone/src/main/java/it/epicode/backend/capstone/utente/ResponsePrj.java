package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.ruoli.Ruoli;

import java.util.List;

public interface ResponsePrj {
    String getUsername();
    String getEmail();
    List<Ruoli> getRoles();
}
