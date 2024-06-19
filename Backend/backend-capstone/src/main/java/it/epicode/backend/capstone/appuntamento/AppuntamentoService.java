package it.epicode.backend.capstone.appuntamento;


import it.epicode.backend.capstone.enums.Stato;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.professionista.ProfessionistaRepository;
import it.epicode.backend.capstone.utente.Utente;
import it.epicode.backend.capstone.utente.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AppuntamentoService {


    @Autowired
    AppuntamentoRepository appuntamentoRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    ProfessionistaRepository professionistaRepository;

    @Transactional
    public Response createAppointment(@Valid Request request) {
        Utente utente = utenteRepository.findById(request.getIdUtente())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        Professionista professionista = professionistaRepository.findById(request.getIdProfessionista())
                .orElseThrow(() -> new RuntimeException("Professionista non trovato"));

        LocalTime oraPrenotazione = LocalTime.parse(request.getOraPrenotazione());
        LocalDateTime dataOraPrenotazione = LocalDateTime.of(request.getDataPrenotazione(), oraPrenotazione);

        Appuntamento appuntamento = new Appuntamento();
        appuntamento.setUtente(utente);
        appuntamento.setProfessionista(professionista);
        appuntamento.setDataPrenotazione(request.getDataPrenotazione());
        appuntamento.setOraPrenotazione(String.valueOf(oraPrenotazione));
        appuntamento.setStato(Stato.RICHIESTO); // O qualsiasi stato iniziale sia necessario
        // Validazione appuntamento
        appuntamento.validateAppuntamento();
        if (!isAppointmentSlotAvailable(appuntamento)) {
            throw new IllegalArgumentException("L'orario richiesto è già occupato.");
        }
        utente.getAppuntamenti().add(appuntamento);
        log.info("appuntamenti"+ utente.getAppuntamenti());
        professionista.getAppuntamenti().add(appuntamento);

        // Salva l'utente e il professionista con l'appuntamento aggiornato
        utenteRepository.save(utente);
        professionistaRepository.save(professionista);

        // Salvataggio appuntamento e creazione risposta
        appuntamentoRepository.save(appuntamento);
        Response response = new Response();
        BeanUtils.copyProperties(appuntamento, response);
        return response;
    }


    public Response updateAppuntamento(Long id, @Valid Request request) {
        if (!appuntamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Appuntamento non trovato");
        }
        if (!professionistaRepository.existsById(request.getIdProfessionista())) {
            throw new EntityNotFoundException("Professionista non trovato");
        }
        if (!utenteRepository.existsById(request.getIdUtente())) {
            throw new EntityNotFoundException("Utente non trovato");
        }
        Appuntamento entity = appuntamentoRepository.findById(id).get();
        Professionista professionista = professionistaRepository.findById(request.getIdProfessionista()).get();
        Utente utente = utenteRepository.findById(request.getIdUtente()).get();

        entity.setProfessionista(professionista);
        entity.setUtente(utente);
        entity.setDataPrenotazione(request.getDataPrenotazione());
        entity.setOraPrenotazione(request.getOraPrenotazione());
        entity.setStato(Stato.RICHIESTO); // O qualsiasi stato sia necessario

        entity.validateAppuntamento();

        if (!isAppointmentSlotAvailable(entity)) {
            throw new IllegalArgumentException("L'orario richiesto è già occupato.");
        }

        appuntamentoRepository.save(entity);

        Response response = new Response();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public String deleteAppointment(Long id) {
      if(!appuntamentoRepository.existsById(id)){
          throw new EntityNotFoundException("Appuntamento non trovato");
      }
      appuntamentoRepository.deleteById(id);
      return "Appuntamento cancellato";
    }

    public Response getAppointmentById(Long id) {
       if(!appuntamentoRepository.existsById(id)){
           throw new EntityNotFoundException("Appuntamento non trovato");
       }
       Appuntamento appuntamento = appuntamentoRepository.findById(id).get();
       Response response = new Response();
       BeanUtils.copyProperties(appuntamento,response);
       return response;
    }

    @Transactional
    public List<ResponsePrj> findAll(){
        // Questo metodo restituisce tutti gli appuntamenti presenti nel database.
        return appuntamentoRepository.findAllBy();
    }

    private boolean isAppointmentSlotAvailable(Appuntamento appuntamento) {
        List<Appuntamento> existingAppointments = appuntamentoRepository
                .findByProfessionistaIdAndDataPrenotazioneAndOraPrenotazione(
                        appuntamento.getProfessionista().getId(),
                        appuntamento.getDataPrenotazione(),
                        appuntamento.getOraPrenotazione()
                );
        return existingAppointments.isEmpty();
    }



    // Metodo per recuperare tutti gli appuntamenti di un utente specifico
    public List<Appuntamento> getAppuntamentiByUtenteId(Long utenteId) {
        return appuntamentoRepository.findByUtenteId(utenteId);
    }

    // Metodo per recuperare tutti gli appuntamenti di un professionista specifico
    public List<Appuntamento> getAppuntamentiByProfessionistaId(Long professionistaId) {
        return appuntamentoRepository.findByProfessionistaId(professionistaId);
    }


}
