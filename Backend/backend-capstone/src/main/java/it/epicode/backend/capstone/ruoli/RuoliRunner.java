package it.epicode.backend.capstone.ruoli;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(6)
public class RuoliRunner implements ApplicationRunner {
    
    @Autowired
    RuoliService ruoliService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Verifica se i ruoli esistono già nel database
        if (!ruoliService.existsByRoleType(Ruoli.ROLES_ADMIN)) {
            ruoliService.create(new Ruoli(Ruoli.ROLES_ADMIN));
            System.out.println("Ruolo ADMIN inserito");
        } else {
            System.out.println("Ruolo ADMIN già presente");
        }

        if (!ruoliService.existsByRoleType(Ruoli.ROLES_UTENTE)) {
            ruoliService.create(new Ruoli(Ruoli.ROLES_UTENTE));
            System.out.println("Ruolo USER inserito");
        } else {
            System.out.println("Ruolo USER già presente");
        }
        if (!ruoliService.existsByRoleType(Ruoli.ROLES_PROFESSIONISTA)) {
            ruoliService.create(new Ruoli(Ruoli.ROLES_PROFESSIONISTA));
            System.out.println("Ruolo PROFESSIONISTA inserito");
        } else {
            System.out.println("Ruolo PROFESSIONISTA già presente");
        }
    }
}

