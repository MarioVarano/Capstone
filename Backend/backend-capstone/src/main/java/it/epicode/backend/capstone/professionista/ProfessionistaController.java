package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.errors.ApiValidationException;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaDTO;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaModel;
import it.epicode.backend.capstone.professionista.Auth.RegisteredProfessionistaDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.ProfessionistaAppuntamentoDTO;
import it.epicode.backend.capstone.utente.Auth.LoginModel;
import it.epicode.backend.capstone.utente.Auth.LoginResponseWrapper;
import it.epicode.backend.capstone.utente.ResponsePrj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/professionista")

public class ProfessionistaController {

    @Autowired
    private ProfessionistaService professionistaService;

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredProfessionistaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(professionistaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponsePrj>> findAll() {
        return ResponseEntity.ok(professionistaService.findAll());
    }



    @PutMapping("/{id}")
    public ResponseEntity<RegisteredProfessionistaDTO> modify(@PathVariable Long id, @RequestBody RegisterProfessionistaModel request) {
        return ResponseEntity.ok(professionistaService.modify(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(professionistaService.delete(id));
    }

    @GetMapping("/{professionistaId}/appuntamenti")
    public List<ProfessionistaAppuntamentoDTO> getAppuntamentiByProfessionistaId(@PathVariable Long professionistaId) {
        return professionistaService.getAppuntamentiByProfessionistaId(professionistaId);
    }

    @PostMapping
    public ResponseEntity<RegisteredProfessionistaDTO> registerProfessional(@RequestBody @Validated RegisterProfessionistaModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }

        var registeredProfessional = professionistaService.registerProfessional(
                RegisterProfessionistaDTO.builder()
                        .withFirstName(model.firstName())
                        .withLastName(model.lastName())
                        .withUsername(model.username())
                        .withEmail(model.email())
                        .withCittà(model.città())
                        .withPassword(model.password())
                        .withSpecializzazione(model.specializzazione())
                        .withDescrizione(model.descrizione())
                        .build());

        return new ResponseEntity<>(registeredProfessional, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginProfessionista(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(professionistaService.loginProfessionista(model.username(), model.password()), HttpStatus.OK);
    }
    @PostMapping("/{id}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String url = professionistaService.uploadProfessionistaAvatar(id, file);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
        }
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<String> deleteAvatar(@PathVariable Long id) {
        try {
            String response = professionistaService.deleteProfessionistaAvatar(id);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete avatar");
        }
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<String> updateAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String url = professionistaService.updateProfessionistaAvatar(id, file);
            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update avatar");
        }
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<String> getAvatar(@PathVariable Long id) {
        String url = professionistaService.getProfessionistaAvatarUrl(id);
        return ResponseEntity.ok(url);
    }





}
