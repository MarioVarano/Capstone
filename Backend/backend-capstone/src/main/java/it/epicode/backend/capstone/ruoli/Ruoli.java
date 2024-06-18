package it.epicode.backend.capstone.ruoli;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ruoli")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ruoli {
    public static final String ROLES_ADMIN = "ADMIN";
    public static final String ROLES_USER = "USER";

    @Id
    private String roleType;
}
