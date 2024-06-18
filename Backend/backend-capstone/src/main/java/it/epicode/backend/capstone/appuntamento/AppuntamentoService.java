package it.epicode.backend.capstone.appuntamento;


import it.epicode.backend.capstone.enums.Stato;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.professionista.ProfessionistaRepository;
import it.epicode.backend.capstone.utente.ResponsePrj;
import it.epicode.backend.capstone.utente.Utente;
import it.epicode.backend.capstone.utente.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class AppuntamentoService {


    @Autowired
    AppuntamentoRepository appuntamentoRepository;

    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    ProfessionistaRepository professionistaRepository;


    public Response createAppointment(Request request) {
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

        // Salvataggio appuntamento e creazione risposta
        appuntamentoRepository.save(appuntamento);
        Response response = new Response();
        BeanUtils.copyProperties(appuntamento, response);
        return response;
    }


    public Response updateAppuntamento(Long id, Request request) {
        if(!appuntamentoRepository.existsById(id)){
            throw new EntityNotFoundException("Appuntamento non trovato");
        }
        if(appuntamentoRepository.existsById(request.getIdProfessionista())){
            throw new EntityNotFoundException("Professionista non trovato");
        }
        if(appuntamentoRepository.existsById(request.getIdUtente())){
            throw new EntityNotFoundException("Utente non trovato");
        }
        Appuntamento entity = appuntamentoRepository.findById(id).get();
        Professionista professionista = professionistaRepository.findById(request.getIdProfessionista()).get();
        Utente utente = utenteRepository.findById(request.getIdUtente()).get();
        BeanUtils.copyProperties(request, entity);
        entity.setProfessionista(professionista);
        entity.setUtente(utente);
        appuntamentoRepository.save(entity);
        entity.validateAppuntamento();

        if (!isAppointmentSlotAvailable(entity)) {
            throw new IllegalArgumentException("L'orario richiesto è già occupato.");
        }
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

    public List<ResponsePrj> findAll(){
        // Questo metodo restituisce tutti gli autori presenti nel database.
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






}
