package it.epicode.backend.capstone.utente;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@Validated
public class UtenteService {


    @Autowired
    UtenteRepository utenteRepository;


    // GET ALL
    public List<ResponsePrj> findAll(){
        // Questo metodo restituisce tutti gli autori presenti nel database.
        return utenteRepository.findAllBy();
    }


    // GET per ID
    public Response findById(Long id){
        // Questo metodo cerca un entity nel database utilizzando l'ID.
        // Se l'entity non esiste, viene generata un'eccezione.
        if(!utenteRepository.existsById(id)){
            throw new EntityNotFoundException("Autore non trovato");
        }
        // Se l'entity esiste, viene recuperato e le sue proprietà vengono copiate in un oggetto AutoreResponse.
        Utente entity = utenteRepository.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    @Transactional
    public Response create(@Valid Request request){ // Annotazione @Valid controlla validazione dto's
        // Questo metodo crea un nuovo entity.
        // Le proprietà dell'entity vengono copiate da un oggetto AutoreRequest.
        Utente entity = new Utente();
        BeanUtils.copyProperties(request, entity);
        // L'entity viene quindi salvato nel database e le sue proprietà vengono copiate in un oggetto AutoreResponse.
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        utenteRepository.save(entity);
        return response;
    }

    // PUT
    public Response modify(Long id, @Valid Request request){
        // Questo metodo modifica un entity esistente.
        // Prima verifica se l'entity esiste nel database. Se non esiste, viene generata un'eccezione.
        if(!utenteRepository.existsById(id)){
            throw new EntityNotFoundException("Autore non trovato");
        }
        // Se l'entity esiste, le sue proprietà vengono modificate con quelle presenti nell'oggetto AutoreRequest.
        Utente entity = utenteRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        // L'entity modificato viene quindi salvato nel database e le sue proprietà vengono copiate in un oggetto AutoreResponse.
        utenteRepository.save(entity);
        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    //DELETE
    public String delete(Long id){
        // Questo metodo elimina un autore dal database.
        // Prima verifica se l'autore esiste nel database. Se non esiste, viene generata un'eccezione.
        if(!utenteRepository.existsById(id)){
            throw  new EntityNotFoundException("Autore non trovato");
        }
        // Se l'autore esiste, viene eliminato dal database.
        utenteRepository.deleteById(id);
        return "Autore eliminato";
    }


}
