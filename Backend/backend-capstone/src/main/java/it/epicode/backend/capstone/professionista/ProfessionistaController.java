package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.errors.ApiValidationException;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaDTO;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaModel;
import it.epicode.backend.capstone.professionista.Auth.RegisteredProfessionistaDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.ProfessionistaAppuntamentoDTO;
import it.epicode.backend.capstone.utente.ResponsePrj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                        .withAvatar(model.avatar())
                        .withCittà(model.città())
                        .withPassword(model.password())
                        .withSpecializzazione(model.specializzazione())
                        .withDescrizione(model.descrizione())
                        .build());

        return new ResponseEntity<>(registeredProfessional, HttpStatus.OK);
    }





}
