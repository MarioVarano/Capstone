package it.epicode.backend.capstone.appuntamento;


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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Appuntamento appuntamento = new Appuntamento();
        BeanUtils.copyProperties(request, appuntamento);
        appuntamento.setUtente(utente);
        appuntamento.setProfessionista(professionista);
        validateAppuntamento(appuntamento);
        if (!isAppointmentSlotAvailable(appuntamento)) {
            throw new IllegalArgumentException("L'orario richiesto è già occupato.");
        }
        Response response = new Response();
        BeanUtils.copyProperties(appuntamento, response);
        appuntamentoRepository.save(appuntamento);
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
        validateAppuntamento(entity);

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

    private void validateAppuntamento(Appuntamento appuntamento) {
        LocalDateTime appointmentDateTime = appuntamento.getDataOra().toLocalDateTime();
        DayOfWeek dayOfWeek = appointmentDateTime.getDayOfWeek();
        int hour = appointmentDateTime.getHour();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new IllegalArgumentException("Gli appuntamenti possono essere presi solo dal lunedì al venerdì.");
        }

        if (hour < 9 || hour >= 17) {
            throw new IllegalArgumentException("Gli appuntamenti possono essere presi solo tra le 9 e le 17.");
        }

        if (!appointmentDateTime.plusHours(1).toLocalDate().equals(appointmentDateTime.toLocalDate())) {
            throw new IllegalArgumentException("Gli appuntamenti devono durare un'ora.");
        }
    }

    private boolean isAppointmentSlotAvailable(Appuntamento appuntamento) {
        List<Appuntamento> existingAppointments = appuntamentoRepository.findByProfessionistaIdAndDataOraBetween(
                appuntamento.getProfessionista().getId(),
                Timestamp.valueOf(appuntamento.getDataOra().toLocalDateTime().withHour(9).withMinute(0).withSecond(0).withNano(0)),
                Timestamp.valueOf(appuntamento.getDataOra().toLocalDateTime().withHour(17).withMinute(0).withSecond(0).withNano(0))
        );

        for (Appuntamento existingAppointment : existingAppointments) {
            if (existingAppointment.getDataOra().equals(appuntamento.getDataOra())) {
                return false;
            }
        }
        return true;
    }


}
