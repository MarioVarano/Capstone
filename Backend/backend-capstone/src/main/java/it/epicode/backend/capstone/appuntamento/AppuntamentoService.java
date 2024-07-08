package it.epicode.backend.capstone.appuntamento;


import it.epicode.backend.capstone.errors.GeneralResponse;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.professionista.ProfessionistaRepository;
import it.epicode.backend.capstone.utente.User;
import it.epicode.backend.capstone.utente.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    UserRepository userRepository;

    @Autowired
    ProfessionistaRepository professionistaRepository;

    @Autowired
    JavaMailSender emailSender;

    @Transactional
    public GeneralResponse<Response> createAppointment(@Valid Request request) {
        try {
            Long userId = request.getIdUtente();
            Long professionalId = request.getIdProfessionista();

            // Verifica che gli ID non siano nulli
            if (userId == null || professionalId == null) {
                log.error("User ID or Professional ID is null. User ID: {}, Professional ID: {}", userId, professionalId);
                throw new IllegalArgumentException("ID utente e professionista non devono essere nulli");
            }

            User utente = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));
            Professionista professionista = professionistaRepository.findById(professionalId)
                    .orElseThrow(() -> new RuntimeException("Professionista non trovato"));

            LocalTime oraPrenotazione = LocalTime.parse(request.getOraPrenotazione());
            LocalDateTime dataOraPrenotazione = LocalDateTime.of(request.getDataPrenotazione(), oraPrenotazione);

            if (utenteHasAppointmentAtTime(utente.getId(), dataOraPrenotazione)) {
                throw new IllegalArgumentException("L'utente ha già un appuntamento a questo orario.");
            }

            Appuntamento appuntamento = new Appuntamento();
            appuntamento.setUtente(utente);
            appuntamento.setProfessionista(professionista);
            appuntamento.setDataPrenotazione(request.getDataPrenotazione());
            appuntamento.setOraPrenotazione(String.valueOf(oraPrenotazione));
            appuntamento.setConfermato(false);

            // Validazione appuntamento
            appuntamento.validateAppuntamento();
            if (isAppointmentSlotAvailable(appuntamento)) {
                throw new IllegalArgumentException("L'orario richiesto è già occupato.");
            }

            // Salvataggio appuntamento
            appuntamentoRepository.save(appuntamento);

            // Invia email di conferma
            sendConfirmationEmailToProfessionista(appuntamento);

            Response response = new Response();
            BeanUtils.copyProperties(appuntamento, response);
            return new GeneralResponse<>(response);
        } catch (Exception e) {
            return new GeneralResponse<>(e.getMessage());
        }
    }


    @Transactional
    public void confirmAppointment(Long id) {
        Appuntamento appuntamento = appuntamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appuntamento non trovato"));

        appuntamento.setConfermato(true);
        appuntamentoRepository.save(appuntamento);

        // Invia email di conferma all'utente
        sendConfirmationEmailToUtente(appuntamento);
    }

    private boolean utenteHasAppointmentAtTime(Long utenteId, LocalDateTime dataOraPrenotazione) {
        return !appuntamentoRepository.findByUtenteIdAndDataPrenotazioneAndOraPrenotazione(
                utenteId, dataOraPrenotazione.toLocalDate(), dataOraPrenotazione.toLocalTime().toString()
        ).isEmpty();
    }


    private boolean isAppointmentSlotAvailable(Appuntamento appuntamento) {
        List<Appuntamento> existingAppointments = appuntamentoRepository
                .findByProfessionistaIdAndDataPrenotazioneAndOraPrenotazione(
                        appuntamento.getProfessionista().getId(),
                        appuntamento.getDataPrenotazione(),
                        appuntamento.getOraPrenotazione()
                );

        List<Appuntamento> userAppointments = appuntamentoRepository
                .findByUtenteIdAndDataPrenotazioneAndOraPrenotazione(
                        appuntamento.getUtente().getId(),
                        appuntamento.getDataPrenotazione(),
                        appuntamento.getOraPrenotazione()
                );
        log.debug("Existing appointments for professional: {}", existingAppointments);
        log.debug("Existing appointments for user: {}", userAppointments);

        return !existingAppointments.isEmpty() || !userAppointments.isEmpty();
    }

    private void sendConfirmationEmailToProfessionista(Appuntamento appuntamento) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appuntamento.getProfessionista().getEmail());
        message.setSubject("Nuovo appuntamento da confermare");
        message.setText("Hai un nuovo appuntamento da confermare. Clicca sul link per confermare: " +
                "http://localhost:8080/api/appuntamento/confirm/" + appuntamento.getId());
        emailSender.send(message);
    }

    private void sendConfirmationEmailToUtente(Appuntamento appuntamento) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appuntamento.getUtente().getEmail());
        message.setSubject("Appuntamento confermato");
        message.setText("Il tuo appuntamento con " + appuntamento.getProfessionista().getFirstName() + " " +
                appuntamento.getProfessionista().getLastName() + " è stato confermato.");
        emailSender.send(message);
    }

    public GeneralResponse<Response> updateAppuntamento(Long id, @Valid Request request) {
        try {
            if (!appuntamentoRepository.existsById(id)) {
                throw new EntityNotFoundException("Appuntamento non trovato");
            }
            if (!professionistaRepository.existsById(request.getIdProfessionista())) {
                throw new EntityNotFoundException("Professionista non trovato");
            }
            if (!userRepository.existsById(request.getIdUtente())) {
                throw new EntityNotFoundException("Utente non trovato");
            }

            Appuntamento entity = appuntamentoRepository.findById(id).get();
            Professionista professionista = professionistaRepository.findById(request.getIdProfessionista()).get();
            User utente = userRepository.findById(request.getIdUtente()).get();

            entity.setProfessionista(professionista);
            entity.setUtente(utente);
            entity.setDataPrenotazione(request.getDataPrenotazione());
            entity.setOraPrenotazione(request.getOraPrenotazione());
            entity.setConfermato(false);

            entity.validateAppuntamento();

            if (isAppointmentSlotAvailable(entity)) {
                throw new IllegalArgumentException("L'orario richiesto è già occupato.");
            }

            appuntamentoRepository.save(entity);

            Response response = new Response();
            BeanUtils.copyProperties(entity, response);
            return new GeneralResponse<>(response);
        } catch (Exception e) {
            return new GeneralResponse<>(e.getMessage());
        }
    }


    public String deleteAppointment(Long id) {
        if (!appuntamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Appuntamento non trovato");
        }
        appuntamentoRepository.deleteById(id);
        return "Appuntamento cancellato";
    }

    public Response getAppointmentById(Long id) {
        if (!appuntamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Appuntamento non trovato");
        }
        Appuntamento appuntamento = appuntamentoRepository.findById(id).get();
        Response response = new Response();
        BeanUtils.copyProperties(appuntamento, response);
        return response;
    }

    @Transactional
    public List<ResponsePrj> findAll() {
        // Questo metodo restituisce tutti gli appuntamenti presenti nel database.
        return appuntamentoRepository.findAllBy();
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
