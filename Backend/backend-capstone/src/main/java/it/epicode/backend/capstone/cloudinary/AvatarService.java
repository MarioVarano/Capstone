package it.epicode.backend.capstone.cloudinary;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.backend.capstone.errors.FileSizeExceededException;
import it.epicode.backend.capstone.professionista.Professionista;
import it.epicode.backend.capstone.professionista.ProfessionistaRepository;
import it.epicode.backend.capstone.utente.User;
import it.epicode.backend.capstone.utente.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessionistaRepository professionistaRepository;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;


    @Transactional
    public UploadAvatarResponse uploadAvatar(Long id, MultipartFile image, boolean isProfessionista) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (image.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds the maximum allowed size");
        }

        String existingPublicId;
        String newPublicId;
        if (isProfessionista) {
            Professionista professionista = professionistaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Professionista not found with id: " + id));
            existingPublicId = professionista.getAvatar();
            newPublicId = uploadImage(image, existingPublicId);
            professionista.setAvatar(newPublicId);
            professionistaRepository.save(professionista);
        } else {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
            existingPublicId = user.getAvatar();
            newPublicId = uploadImage(image, existingPublicId);
            user.setAvatar(newPublicId);
            userRepository.save(user);
        }
        UploadAvatarResponse response = new UploadAvatarResponse();
        // Recupera l'URL dell'immagine caricata
        response.setUrl(cloudinary.url().publicId(newPublicId).generate());
        return response;
    }

    @Transactional
    public String deleteAvatar(Long id, boolean isProfessionista) throws IOException {
        String publicId;
        if (isProfessionista) {
            Professionista professionista = professionistaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Professionista not found with id: " + id));
            publicId = professionista.getAvatar();
            deleteImage(publicId);
            professionista.setAvatar(null);
            professionistaRepository.save(professionista);
        } else {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
            publicId = user.getAvatar();
            deleteImage(publicId);
            user.setAvatar(null);
            userRepository.save(user);
        }

        return "Avatar deleted successfully";
    }

    @Transactional
    public UploadAvatarResponse updateAvatar(Long id, MultipartFile updatedImage, boolean isProfessionista) throws IOException {
        deleteAvatar(id, isProfessionista);
        return uploadAvatar(id, updatedImage, isProfessionista);
    }

    @Transactional
    public String getAvatarUrl(Long id, boolean isProfessionista) {
        String publicId;
        if (isProfessionista) {
            Professionista professionista = professionistaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Professionista not found with id: " + id));
            publicId = professionista.getAvatar();
        } else {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
            publicId = user.getAvatar();
        }

        return cloudinary.url().publicId(publicId).generate();
    }

    private String uploadImage(MultipartFile image, String existingPublicId) throws IOException {
        if (existingPublicId != null && !existingPublicId.isEmpty()) {
            cloudinary.uploader().destroy(existingPublicId, ObjectUtils.emptyMap());
        }
        var uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }

    private void deleteImage(String publicId) throws IOException {
        if (publicId != null && !publicId.isEmpty()) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }
}
