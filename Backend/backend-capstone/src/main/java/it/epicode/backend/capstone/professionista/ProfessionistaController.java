package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.professionista.appuntamentoDTO.ProfessionistaAppuntamentoDTO;
import it.epicode.backend.capstone.utente.ResponsePrj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professionista")

public class ProfessionistaController {

    @Autowired
    private ProfessionistaService professionistaService;

    @GetMapping("/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(professionistaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ResponsePrj>> findAll() {
        return ResponseEntity.ok(professionistaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Response> create(@RequestBody Request request) {
        return ResponseEntity.ok(professionistaService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> modify(@PathVariable Long id, @RequestBody Request request) {
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
}
