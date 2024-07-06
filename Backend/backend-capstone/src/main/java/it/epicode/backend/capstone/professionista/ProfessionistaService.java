package it.epicode.backend.capstone.professionista;

import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.appuntamento.AppuntamentoRepository;
import it.epicode.backend.capstone.cloudinary.AvatarService;
import it.epicode.backend.capstone.cloudinary.UploadAvatarResponse;
import it.epicode.backend.capstone.email.EmailService;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaDTO;
import it.epicode.backend.capstone.professionista.Auth.RegisterProfessionistaModel;
import it.epicode.backend.capstone.professionista.Auth.RegisteredProfessionistaDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.ProfessionistaAppuntamentoDTO;
import it.epicode.backend.capstone.professionista.appuntamentoDTO.UtenteDTO;
import it.epicode.backend.capstone.ruoli.Ruoli;
import it.epicode.backend.capstone.ruoli.RuoliRepository;
import it.epicode.backend.capstone.security.JwtUtils;
import it.epicode.backend.capstone.utente.ResponsePrj;
import it.epicode.backend.capstone.utente.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfessionistaService {

    private final JwtUtils jwt;
    private final UserRepository userRepository;
    private final RuoliRepository ruoliRepository;
    private final ProfessionistaRepository professionistaRepository;
    private final AppuntamentoRepository appuntamentoRepository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final AuthenticationManager auth;
    private final AvatarService avatarService;

    public List<Professionista> findAll() {
        return professionistaRepository.findAll();
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
    public RegisteredProfessionistaDTO modify(Long id, RegisterProfessionistaModel registerProfessionistaModel) {
        Professionista entity = professionistaRepository.findById(id).get();

        // Copia le proprietà dall'oggetto modello all'entità
        BeanUtils.copyProperties(registerProfessionistaModel, entity, "password");

        // Gestisci la password separatamente
        if (registerProfessionistaModel.password() != null && !registerProfessionistaModel.password().isEmpty()) {
            String pass = encoder.encode(registerProfessionistaModel.password());
            entity.setPassword(pass);
        }

        // Salva l'entità modificata nel database
        professionistaRepository.save(entity);

        // Crea e restituisci il DTO
        RegisteredProfessionistaDTO registeredProfessionistaDTO = new RegisteredProfessionistaDTO();
        BeanUtils.copyProperties(entity, registeredProfessionistaDTO);
        return registeredProfessionistaDTO;
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




    public UploadAvatarResponse uploadProfessionistaAvatar(Long id, MultipartFile image) throws IOException {
        return avatarService.uploadAvatar(id, image, true);
    }

    public String getProfessionistaAvatarUrl(Long id) {
        return avatarService.getAvatarUrl(id, true);
    }

    public String deleteProfessionistaAvatar(Long id) throws IOException {
        return avatarService.deleteAvatar(id, true);
    }

    public UploadAvatarResponse updateProfessionistaAvatar(Long id, MultipartFile image) throws IOException {
        return avatarService.updateAvatar(id, image, true);
    }

    public List<Professionista> findByCity(String city) {
        return professionistaRepository.findByCity(city);
    }

    public List<Professionista> findBySpecializzazione(String specializzazione) {
        return professionistaRepository.findBySpecializzazione(specializzazione);
    }
}

