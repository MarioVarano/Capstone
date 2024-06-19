package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.appuntamento.AppuntamentoRepository;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.ProfessionistaAppuntamentoDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.UtenteDTO;
import it.epicode.backend.capstone.utente.ResponsePrj;
import it.epicode.backend.capstone.utente.Utente;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionistaService {
    @Autowired
    private ProfessionistaRepository professionistaRepository;

    @Autowired
    AppuntamentoRepository appuntamentoRepository;

    public List<ResponsePrj> findAll() {
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
    public List<ProfessionistaAppuntamentoDTO> getAppuntamentiByProfessionistaId(Long professionistaId) {
        List<Appuntamento> appuntamenti = appuntamentoRepository.findByProfessionistaId(professionistaId);
        return appuntamenti.stream().map(this::convertToProfessionistaAppuntamentoDTO).collect(Collectors.toList());
    }

    private ProfessionistaAppuntamentoDTO convertToProfessionistaAppuntamentoDTO(Appuntamento appuntamento) {
        ProfessionistaAppuntamentoDTO dto = new ProfessionistaAppuntamentoDTO();
        dto.setId(appuntamento.getId());
        dto.setDataPrenotazione(appuntamento.getDataPrenotazione().toString());
        dto.setOraPrenotazione(appuntamento.getOraPrenotazione());
        dto.setStato(appuntamento.getStato());

        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setId(appuntamento.getUtente().getId());
        utenteDTO.setNome(appuntamento.getUtente().getNome());
        utenteDTO.setCognome(appuntamento.getUtente().getCognome());
        utenteDTO.setEmail(appuntamento.getUtente().getEmail());

        dto.setUtente(utenteDTO);
        return dto;
    }
}

