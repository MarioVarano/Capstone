package it.epicode.backend.capstone.utente;


import it.epicode.backend.capstone.errors.ApiValidationException;
import it.epicode.backend.capstone.utente.Auth.*;
import it.epicode.backend.capstone.utente.appuntamentoDTO.UtenteAppuntamentoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService user;

    // Metodo per ottenere un autore specifico in base all'ID. Quando si effettua una richiesta GET a /api/autori/{id}, questo metodo viene chiamato e restituisce l'autore con l'ID specificato.
    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(user.findById(id));
    }

    // Metodo per ottenere tutti gli autori. Quando si effettua una richiesta GET a /api/autori, questo metodo viene chiamato e restituisce una lista di tutti gli autori.
    @GetMapping
    public ResponseEntity<List<ResponsePrj>> findAll(){
        return ResponseEntity.ok(user.findAll());
    }


    // Metodo per modificare un autore esistente. Quando si effettua una richiesta PUT a /api/autori/{id} con i nuovi dettagli dell'autore nel corpo della richiesta, questo metodo viene chiamato e modifica l'autore con l'ID specificato.
    @PutMapping("/{id}")
    public ResponseEntity<RegisteredUserDTO> modify(@PathVariable Long id, @RequestBody RegisterUserModel request){
        return ResponseEntity.ok(user.modify(id, request));
    }

    // Metodo per eliminare un autore. Quando si effettua una richiesta DELETE a /api/autori/{id}, questo metodo viene chiamato e elimina l'autore con l'ID specificato.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseEntity.ok(user.delete(id));
    }
    @GetMapping("/{utenteId}/appuntamenti")
    public List<UtenteAppuntamentoDTO> getAppuntamentiByUtenteId(@PathVariable Long utenteId) {
        return user.getAppuntamentiByUtenteId(utenteId);
    }
    @PostMapping
    public ResponseEntity<RegisteredUserDTO> register(@RequestBody @Validated RegisterUserModel model, BindingResult validator){
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        var registeredUser = user.register(
                RegisterUserDTO.builder()
                        .withFirstName(model.firstName())
                        .withLastName(model.lastName())
                        .withUsername(model.username())
                        .withEmail(model.email())
                        .withAvatar(model.avatar())
                        .withCittà(model.città())
                        .withPassword(model.password())
                        .build());

        return  new ResponseEntity<> (registeredUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseWrapper> login(@RequestBody @Validated LoginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        return new ResponseEntity<>(user.login(model.username(), model.password()).orElseThrow(), HttpStatus.OK);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<RegisteredUserDTO> registerAdmin(@RequestBody RegisterUserDTO registerUser){
        return ResponseEntity.ok(user.registerAdmin(registerUser));
    }
}


