package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.appuntamento.AppuntamentoRepository;
import it.epicode.backend.capstone.email.EmailService;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaDTO;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaModel;
import it.epicode.backend.capstone.professionista.Auth.RegisteredProfessionistaDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.ProfessionistaAppuntamentoDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.UtenteDTO;
import it.epicode.backend.capstone.ruoli.Ruoli;
import it.epicode.backend.capstone.ruoli.RuoliRepository;
import it.epicode.backend.capstone.utente.Auth.RegisteredUserDTO;
import it.epicode.backend.capstone.utente.ResponsePrj;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessionistaService {
    @Autowired
    RuoliRepository ruoliRepository;

    @Autowired
    private ProfessionistaRepository professionistaRepository;

    @Autowired
    AppuntamentoRepository appuntamentoRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    public List<ResponsePrj> findAll() {
        return professionistaRepository.findAllBy();
    }

    public RegisteredProfessionistaDTO findById(Long id) {
        if(!professionistaRepository.existsById(id)){
            throw new EntityNotFoundException("Autore non trovato");
        }
        // Se l'entity esiste, viene recuperato e le sue proprietà vengono copiate in un oggetto AutoreRegisteredProfessionistaDTO.
        Professionista entity = professionistaRepository.findById(id).get();
        RegisteredProfessionistaDTO RegisteredProfessionistaDTO = new RegisteredProfessionistaDTO();
        BeanUtils.copyProperties(entity, RegisteredProfessionistaDTO);
        return RegisteredProfessionistaDTO;
    }

    @Transactional
    public RegisteredProfessionistaDTO create(@Valid RegisterProfessionistaDTO RegisterProfessionistaDTO) {
        Professionista entity = new Professionista();
        BeanUtils.copyProperties(RegisterProfessionistaDTO, entity);
        professionistaRepository.save(entity);
        RegisteredProfessionistaDTO RegisteredProfessionistaDTO = new RegisteredProfessionistaDTO();
        BeanUtils.copyProperties(entity, RegisteredProfessionistaDTO);
        return RegisteredProfessionistaDTO;

    }

    @Transactional
    public RegisteredProfessionistaDTO modify(Long id, RegisterProfessionistaModel RegisterProfessionistaDTO) {
        Professionista entity = professionistaRepository.findById(id).get();
        BeanUtils.copyProperties(RegisterProfessionistaDTO, entity);
        // L'entity modificato viene quindi salvato nel database e le sue proprietà vengono copiate in un oggetto AutoreRegisteredProfessionistaDTO.
        professionistaRepository.save(entity);
       RegisteredProfessionistaDTO RegisteredProfessionistaDTO = new RegisteredProfessionistaDTO();
        BeanUtils.copyProperties(entity, RegisteredProfessionistaDTO);
        return RegisteredProfessionistaDTO;
    }

    public String delete(Long id) {
        if (!professionistaRepository.existsById(id)) {
            throw new EntityNotFoundException("Professionista non trovato");
        }
        professionistaRepository.deleteById(id);
        return "Professionista eliminato";
    }
    public List<ProfessionistaAppuntamentoDTO> getAppuntamentiByProfessionistaId(Long professionistaId) {
        List<Appuntamento> appuntamenti = appuntamentoRepository.findByProfessionistaId(professionistaId);
        return appuntamenti.stream().map(this::convertToProfessionistaAppuntamentoDTO).collect(Collectors.toList());
    }

    private ProfessionistaAppuntamentoDTO convertToProfessionistaAppuntamentoDTO(Appuntamento appuntamento) {
        ProfessionistaAppuntamentoDTO dto = new ProfessionistaAppuntamentoDTO();
        dto.setId(appuntamento.getId());
        dto.setDataPrenotazione(appuntamento.getDataPrenotazione().toString());
        dto.setOraPrenotazione(appuntamento.getOraPrenotazione());
        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setId(appuntamento.getUtente().getId());
        utenteDTO.setNome(appuntamento.getUtente().getFirstName());
        utenteDTO.setCognome(appuntamento.getUtente().getLastName());
        utenteDTO.setEmail(appuntamento.getUtente().getEmail());

        dto.setUtente(utenteDTO);
        return dto;
    }

    public RegisteredProfessionistaDTO registerProfessional(RegisterProfessionistaDTO register) {
        if(professionistaRepository.existsByUsername(register.getUsername())) {
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(professionistaRepository.existsByEmail(register.getEmail())) {
            throw new EntityExistsException("Email gia' registrata");
        }
        Ruoli roles = ruoliRepository.findById(Ruoli.ROLES_PROFESSIONISTA).get();

        Professionista professional = new Professionista();
        BeanUtils.copyProperties(register, professional);
        professional.setPassword(encoder.encode(register.getPassword()));
        professional.getRoles().add(roles);
        professional.setSpecializzazione(register.getSpecializzazione());
        professional.setDescrizione(register.getDescrizione());
        professionistaRepository.save(professional);

        RegisteredProfessionistaDTO response = new RegisteredProfessionistaDTO();
        BeanUtils.copyProperties(professional, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(professional.getEmail());

        return response;
    }

}

