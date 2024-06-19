package it.epicode.backend.capstone.appuntamento;

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
    @GetMapping("/all")
    public ResponseEntity<List<ResponsePrj>> findAllAppuntamenti() {
        List<ResponsePrj> response = appuntamentoService.findAll();
        return ResponseEntity.ok(response);
    }

    // Metodo per creare un nuovo appuntamento.
    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Request request) {
        return ResponseEntity.ok(appuntamentoService.createAppointment(request));
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
}

