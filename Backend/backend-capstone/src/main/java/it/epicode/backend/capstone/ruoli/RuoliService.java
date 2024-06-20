package it.epicode.backend.capstone.ruoli;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuoliService {


    @Autowired
    private RuoliRepository rolesRepository;



    public Ruoli create(Ruoli ruoli){
        return rolesRepository.save(ruoli);
    }

    public boolean existsByRoleType(String roleType) {
        return rolesRepository.existsById(roleType);
    }
}
