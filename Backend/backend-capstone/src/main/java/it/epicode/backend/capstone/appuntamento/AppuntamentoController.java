package it.epicode.backend.capstone.appuntamento;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.epicode.backend.capstone.errors.GeneralResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appuntamento")
public class AppuntamentoController {
    @Autowired
    AppuntamentoService appuntamentoService;

    @Operation(summary = "Ottieni un appuntamento specifico per ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamento trovato"),
            @ApiResponse(responseCode = "404", description = "Appuntamento non trovato")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@Parameter(description = "ID dell'appuntamento") @PathVariable Long id) {
        return ResponseEntity.ok(appuntamentoService.getAppointmentById(id));
    }

    @Operation(summary = "Ottieni tutti gli appuntamenti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamenti trovati"),
            @ApiResponse(responseCode = "404", description = "Nessun appuntamento trovato")
    })
    @GetMapping
    public ResponseEntity<List<ResponsePrj>> findAllAppuntamenti() {
        return ResponseEntity.ok(appuntamentoService.findAll());
    }

    @Operation(summary = "Crea un nuovo appuntamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamento creato con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida")
    })
    @PostMapping
    public ResponseEntity<GeneralResponse<Response>> create(
            @Parameter(description = "Dettagli della richiesta di appuntamento") @RequestBody Request request) {
        GeneralResponse<Response> response = appuntamentoService.createAppointment(request);
        if (response.getErrorMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Modifica un appuntamento esistente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamento modificato con successo"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida"),
            @ApiResponse(responseCode = "404", description = "Appuntamento non trovato")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse<Response>> modify(
            @Parameter(description = "ID dell'appuntamento") @PathVariable Long id,
            @Parameter(description = "Dettagli della modifica dell'appuntamento") @RequestBody Request request) {
        GeneralResponse<Response> response = appuntamentoService.updateAppuntamento(id, request);
        if (response.getErrorMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Elimina un appuntamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamento eliminato con successo"),
            @ApiResponse(responseCode = "404", description = "Appuntamento non trovato")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @Parameter(description = "ID dell'appuntamento") @PathVariable Long id) {
        return ResponseEntity.ok(appuntamentoService.deleteAppointment(id));
    }

    @Operation(summary = "Ottieni appuntamenti per un utente specifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamenti trovati"),
            @ApiResponse(responseCode = "404", description = "Nessun appuntamento trovato")
    })
    @GetMapping("/utente/{utenteId}")
    public List<Appuntamento> getAppuntamentiByUtenteId(
            @Parameter(description = "ID dell'utente") @PathVariable Long utenteId) {
        return appuntamentoService.getAppuntamentiByUtenteId(utenteId);
    }

    @Operation(summary = "Ottieni appuntamenti per un professionista specifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamenti trovati"),
            @ApiResponse(responseCode = "404", description = "Nessun appuntamento trovato")
    })
    @GetMapping("/professionista/{professionistaId}")
    public List<Appuntamento> getAppuntamentiByProfessionistaId(
            @Parameter(description = "ID del professionista") @PathVariable Long professionistaId) {
        return appuntamentoService.getAppuntamentiByProfessionistaId(professionistaId);
    }

    @Operation(summary = "Conferma un appuntamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appuntamento confermato con successo"),
            @ApiResponse(responseCode = "401", description = "Errore nella conferma dell'appuntamento")
    })
    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmAppointment(
            @Parameter(description = "ID dell'appuntamento") @PathVariable Long id) {
        try {
            appuntamentoService.confirmAppointment(id);
            return ResponseEntity.ok("Appuntamento confermato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Errore nella conferma dell'appuntamento.");
        }
    }
}
