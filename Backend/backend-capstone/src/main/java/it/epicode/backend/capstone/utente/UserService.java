package it.epicode.backend.capstone.utente;


import com.cloudinary.Cloudinary;
import it.epicode.backend.capstone.appuntamento.Appuntamento;
import it.epicode.backend.capstone.appuntamento.AppuntamentoRepository;
import it.epicode.backend.capstone.cloudinary.AvatarService;
import it.epicode.backend.capstone.cloudinary.UploadAvatarResponse;
import it.epicode.backend.capstone.email.EmailService;
import it.epicode.backend.capstone.errors.InvalidLoginException;
import it.epicode.backend.capstone.professionista.Auth.LoginResponseProfessionistaDTO;
import it.epicode.backend.capstone.professionista.Auth.RegisteredProfessionistaDTO;
import it.epicode.backend.capstone.professionista.ProfessionistaRepository;
import it.epicode.backend.capstone.ruoli.Ruoli;
import it.epicode.backend.capstone.ruoli.RuoliRepository;
import it.epicode.backend.capstone.security.JwtUtils;
import it.epicode.backend.capstone.utente.Auth.*;
import it.epicode.backend.capstone.utente.appuntamentoDTO.ProfessionistaDTO;
import it.epicode.backend.capstone.utente.appuntamentoDTO.UtenteAppuntamentoDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserService {


    private final ProfessionistaRepository professionistaRepository;

    private final UserRepository userRepository;
    private final AppuntamentoRepository appuntamentoRepository;
    private final PasswordEncoder encoder;
    private final RuoliRepository rolesRepository;
    private final AuthenticationManager auth;
    private final JwtUtils jwt;
    private final EmailService emailService; // per gestire invio email di benvenuto
    private final Cloudinary cloudinary;
    private final AvatarService avatarService;



    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;


    // GET ALL
    public List<ResponsePrj> findAll(){
        // Questo metodo restituisce tutti gli autori presenti nel database.
        return userRepository.findAllBy();
    }


    // GET per ID
    public RegisteredUserDTO findById(Long id){
        // Questo metodo cerca un entity nel database utilizzando l'ID.
        // Se l'entity non esiste, viene generata un'eccezione.
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("Autore non trovato");
        }
        // Se l'entity esiste, viene recuperato e le sue proprietà vengono copiate in un oggetto AutoreRegisterdUserDTO.
        User entity = userRepository.findById(id).get();
        RegisteredUserDTO RegisterdUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(entity, RegisterdUserDTO);
        return RegisterdUserDTO;
    }



    @Transactional
    public RegisteredUserDTO modify(Long id,  RegisterUserModel RegisterUserModel){
        // Questo metodo modifica un entity esistente.
        // Prima verifica se l'entity esiste nel database. Se non esiste, viene generata un'eccezione.
        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("Utente non trovato");
        }
        // Se l'entity esiste, le sue proprietà vengono modificate con quelle presenti nell'oggetto AutoreRegisterUserModel.
        User entity = userRepository.findById(id).get();
        BeanUtils.copyProperties(RegisterUserModel, entity, "password");
        if (RegisterUserModel.password() != null && !RegisterUserModel.password().isEmpty()) {
            String pass = encoder.encode(RegisterUserModel.password());
            entity.setPassword(pass);
        }
        // L'entity modificato viene quindi salvato nel database e le sue proprietà vengono copiate in un oggetto AutoreRegisterdUserDTO.
        userRepository.save(entity);
        RegisteredUserDTO RegisterdUserDTO = new RegisteredUserDTO();
        BeanUtils.copyProperties(entity, RegisterdUserDTO);
        return RegisterdUserDTO;
    }

    //DELETE
    public String delete(Long id){
        // Questo metodo elimina un autore dal database.
        // Prima verifica se l'autore esiste nel database. Se non esiste, viene generata un'eccezione.
        if(!userRepository.existsById(id)){
            throw  new EntityNotFoundException("Autore non trovato");
        }
        // Se l'autore esiste, viene eliminato dal database.
        userRepository.deleteById(id);
        return "Utente eliminato";
    }
    public List<UtenteAppuntamentoDTO> getAppuntamentiByUtenteId(Long utenteId) {
        List<Appuntamento> appuntamenti = appuntamentoRepository.findByUtenteId(utenteId);
        return appuntamenti.stream().map(this::convertToUtenteAppuntamentoDTO).collect(Collectors.toList());
    }

    private UtenteAppuntamentoDTO convertToUtenteAppuntamentoDTO(Appuntamento appuntamento) {
        UtenteAppuntamentoDTO dto = new UtenteAppuntamentoDTO();
        dto.setId(appuntamento.getId());
        dto.setDataPrenotazione(appuntamento.getDataPrenotazione().toString());
        dto.setOraPrenotazione(appuntamento.getOraPrenotazione());

        ProfessionistaDTO professionistaDTO = new ProfessionistaDTO();
        professionistaDTO.setId(appuntamento.getProfessionista().getId());
        professionistaDTO.setNome(appuntamento.getProfessionista().getFirstName());
        professionistaDTO.setCognome(appuntamento.getProfessionista().getLastName());
        professionistaDTO.setSpecializzazione(appuntamento.getProfessionista().getSpecializzazione());

        dto.setProfessionista(professionistaDTO);
        return dto;
    }


    public Optional<LoginResponseWrapper> login(String username, String password) {
        try {
            //SI EFFETTUA IL LOGIN
            //SI CREA UNA AUTENTICAZIONE OVVERO L'OGGETTO DI TIPO AUTHENTICATION
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            a.getAuthorities(); //SERVE A RECUPERARE I RUOLI/IL RUOLO

            //SI CREA UN CONTESTO DI SICUREZZA CHE SARA UTILIZZATO IN PIU OCCASIONI
            SecurityContextHolder.getContext().setAuthentication(a);

            var user = userRepository.findOneByUsername(username).orElseThrow();
            if (user.getRoles().contains(new Ruoli(Ruoli.ROLES_UTENTE)) || user.getRoles().contains(new Ruoli(Ruoli.ROLES_ADMIN))){
                var dto = LoginResponseDTO.builder()
                        .withUser(RegisteredUserDTO.builder()
                                .withId(user.getId())
                                .withFirstName(user.getFirstName())
                                .withLastName(user.getLastName())
                                .withEmail(user.getEmail())
                                .withRoles(user.getRoles())
                                .withUsername(user.getUsername())
                                .withCity(user.getCity())
                                .build())
                        .build();
                dto.setToken(jwt.generateToken(a));
                dto.setSpecializzazione(Ruoli.ROLES_UTENTE);
                return Optional.of(new LoginResponseWrapper(dto));
            } else if (user.getRoles().contains(new Ruoli(Ruoli.ROLES_PROFESSIONISTA))){
                var prof = professionistaRepository.findById(user.getId()).get();
                var dto = LoginResponseProfessionistaDTO.builder()
                        .withProfessionista(RegisteredProfessionistaDTO.builder()
                                .withId(prof.getId())
                                .withFirstName(prof.getFirstName())
                                .withLastName(prof.getLastName())
                                .withEmail(prof.getEmail())
                                .withCity(prof.getCity())
                                .withRoles(prof.getRoles())
                                .withUsername(prof.getUsername())
                                .withSpecializzazione(prof.getSpecializzazione())
                                .build())
                        .build();
                dto.setToken(jwt.generateToken(a));
                dto.setSpecializzazione(Ruoli.ROLES_PROFESSIONISTA);
                return Optional.of(new LoginResponseWrapper(dto));
            }

            //UTILIZZO DI JWTUTILS PER GENERARE IL TOKEN UTILIZZANDO UNA AUTHENTICATION E LO ASSEGNA ALLA LOGINRESPONSEDTO

        } catch (NoSuchElementException e) {
            //ECCEZIONE LANCIATA SE LO USERNAME E SBAGLIATO E QUINDI L'UTENTE NON VIENE TROVATO
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            //ECCEZIONE LANCIATA SE LA PASSWORD E SBAGLIATA
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
        return Optional.empty();
    }



    //metodo per loggare solo lo user, funziona anche quello che seleziona il tipo di utente
    @Transactional
    public String loginUser(String username, String password) {
        try {
            // Effettua il login
            log.debug("Tentativo di autenticazione per username: {}", username);
            var authentication = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Imposta il contesto di sicurezza
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new NoSuchElementException("Utente non trovato"));
            log.debug("Utente trovato: {}", user);

            if (user.getRoles().stream().anyMatch(role -> role.getRoleType().equals(Ruoli.ROLES_UTENTE) || role.getRoleType().equals(Ruoli.ROLES_ADMIN))) {
                return jwt.generateToken(authentication);
            }
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }
        return null;
    }


    public RegisteredUserDTO register(RegisterUserDTO register){
        if(userRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(userRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Ruoli roles = rolesRepository.findById(Ruoli.ROLES_UTENTE).get();
        /*
        if(!rolesRepository.existsById(Roles.ROLES_USER)){
            roles = new Roles();
            roles.setRoleType(Roles.ROLES_USER);
        } else {
            roles = rolesRepository.findById(Roles.ROLES_USER).get();
        }

         */
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        userRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(u.getEmail());

        return response;

    }

    public RegisteredUserDTO registerAdmin(RegisterUserDTO register){
        if(userRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }
        if(userRepository.existsByEmail(register.getEmail())){
            throw new EntityExistsException("Email gia' registrata");
        }
        Ruoli roles = rolesRepository.findById(Ruoli.ROLES_ADMIN).get();
        User u = new User();
        BeanUtils.copyProperties(register, u);
        u.setPassword(encoder.encode(register.getPassword()));
        u.getRoles().add(roles);
        userRepository.save(u);
        RegisteredUserDTO response = new RegisteredUserDTO();
        BeanUtils.copyProperties(u, response);
        response.setRoles(List.of(roles));
        emailService.sendWelcomeEmail(u.getEmail());
        return response;

    }
    public UploadAvatarResponse uploadUserAvatar(Long id, MultipartFile image) throws IOException {
        return avatarService.uploadAvatar(id, image, false);
    }

    public String getUserAvatarUrl(Long id) {
        return avatarService.getAvatarUrl(id, false);
    }

    public String deleteUserAvatar(Long id) throws IOException {
        return avatarService.deleteAvatar(id, false);
    }

    public UploadAvatarResponse updateUserAvatar(Long id, MultipartFile image) throws IOException {
        return avatarService.updateAvatar(id, image, false);
    }

    public Optional<User> findOneByUsername(String username) {
        return Optional.empty();
    }
}
