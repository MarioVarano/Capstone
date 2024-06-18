package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.utente.Utente;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ProfessionistaService {
    @Autowired
    private ProfessionistaRepository professionistaRepository;

    public List<Response> findAll() {
        return professionistaRepository.findAllBy();
    }

    public Response findById(Long id) {
        if(!professionistaRepository.existsById(id)){
            throw new EntityNotFoundException("Autore non trovato");
        }
        // Se l'entity esiste, viene recuperato e le sue proprietà vengono copiate in un oggetto AutoreResponse.
        Professionista entity = professionistaRepository.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    @Transactional
    public Response create(@Valid Request request) {
        Professionista entity = new Professionista();
        BeanUtils.copyProperties(request, entity);
        professionistaRepository.save(entity);
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;

    }

    @Transactional
    public Response modify(Long id, @Valid Request request) {
        Professionista entity = professionistaRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        // L'entity modificato viene quindi salvato nel database e le sue proprietà vengono copiate in un oggetto AutoreResponse.
        professionistaRepository.save(entity);
       Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public String delete(Long id) {
        if (!professionistaRepository.existsById(id)) {
            throw new EntityNotFoundException("Professionista non trovato");
        }
        professionistaRepository.deleteById(id);
        return "Professionista eliminato";
    }

}

