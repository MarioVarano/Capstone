package it.epicode.backend.capstone.utente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    List<ResponsePrj> findAllBy();
    User findByEmail(String email);


    Optional<User> findOneByUsername(String username);


    boolean existsByUsername(String username);



    boolean existsByEmail(String email);

}
