package it.epicode.backend.capstone.appuntamento;

import it.epicode.backend.capstone.errors.GeneralResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appuntamento")
public class AppuntamentoController {
    @Autowired
    AppuntamentoService appuntamentoService;

    // Metodo per ottenere un appuntamento specifico in base all'ID.
    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appuntamentoService.getAppointmentById(id));
    }

    // Metodo per ottenere tutti gli appuntamenti.
    @GetMapping
    public ResponseEntity<List<ResponsePrj>> findAllAppuntamenti() {
        return ResponseEntity.ok(appuntamentoService.findAll());
    }

    // Metodo per creare un nuovo appuntamento.
    @PostMapping
    public ResponseEntity<GeneralResponse<Response>> create(@RequestBody Request request) {
        GeneralResponse<Response> response = appuntamentoService.createAppointment(request);
        if (response.getErrorMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


    // Metodo per modificare un appuntamento esistente.
    @PutMapping("/{id}")
    public ResponseEntity<Response> modify(@PathVariable Long id, @RequestBody Request request) {
        return ResponseEntity.ok(appuntamentoService.updateAppuntamento(id, request));
    }

    // Metodo per eliminare un appuntamento.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(appuntamentoService.deleteAppointment(id));
    }

    @GetMapping("/utente/{utenteId}")
    public List<Appuntamento> getAppuntamentiByUtenteId(@PathVariable Long utenteId) {
        return appuntamentoService.getAppuntamentiByUtenteId(utenteId);
    }

    // Endpoint per recuperare tutti gli appuntamenti di un professionista
    @GetMapping("/professionista/{professionistaId}")
    public List<Appuntamento> getAppuntamentiByProfessionistaId(@PathVariable Long professionistaId) {
        return appuntamentoService.getAppuntamentiByProfessionistaId(professionistaId);
    }

    // Endpoint per confermare un appuntamento
    @GetMapping("/confirm/{id}")
    public ResponseEntity<String> confirmAppointment(@PathVariable Long id) {
        appuntamentoService.confirmAppointment(id);
        return ResponseEntity.ok("Appuntamento confermato.");
    }






}

