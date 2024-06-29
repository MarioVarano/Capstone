package it.epicode.backend.capstone.utente;

import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaDTO;
import it.epicode.backend.capstone.professionista.ProfessionistaService;
import it.epicode.backend.capstone.utente.Auth.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class DataRunner implements ApplicationRunner {


    @Autowired
    private UserService userService;

    @Autowired
    private ProfessionistaService professionistaService;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUsers();
        createProfessionisti();
    }

    private void createUsers() {
        userService.register(createUser("John", "Doe", "johndoe", "johndoe@example.com", "password1", "New York"));
        userService.register(createUser("Jane", "Doe", "janedoe", "janedoe@example.com", "password2", "Los Angeles"));
        userService.register(createUser("Jim", "Beam", "jimbeam", "jimbeam@example.com", "password3", "Chicago"));
        userService.register(createUser("Jack", "Daniels", "jackdaniels", "jackdaniels@example.com", "password4", "Chicago"));
    }

    private void createProfessionisti() {
        professionistaService.registerProfessional(createProfessionista("Mario", "Rossi", "mariorossi", "mariorossi@example.com", "password5", "Roma", "Medico Generico", "Esperto in medicina generale"));
        professionistaService.registerProfessional(createProfessionista("Luigi", "Verdi", "luigiverdi", "luigiverdi@example.com", "password6", "Milano", "Dentista", "Specializzato in odontoiatria"));
        professionistaService.registerProfessional(createProfessionista("Anna", "Bianchi", "annabianchi", "annabianchi@example.com", "password7", "Firenze", "Cardiologo", "Specialista in cardiologia"));
        professionistaService.registerProfessional(createProfessionista("Marco", "Neri", "marconeri", "marconeri@example.com", "password8", "Milano", "Pediatra", "Esperto in pediatria"));
    }

    private RegisterUserDTO createUser(String firstName, String lastName, String username, String email, String password, String citta) {
        return RegisterUserDTO.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withUsername(username)
                .withEmail(email)
                .withCity(citta)
                .withPassword(password)
                .build();
    }

    private RegisterProfessionistaDTO createProfessionista(String firstName, String lastName, String username, String email, String password, String citta, String specializzazione, String descrizione) {
        return RegisterProfessionistaDTO.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withUsername(username)
                .withEmail(email)
                .withCity(citta)
                .withPassword(password)
                .withSpecializzazione(specializzazione)
                .withDescrizione(descrizione)
                .build();
    }
}
