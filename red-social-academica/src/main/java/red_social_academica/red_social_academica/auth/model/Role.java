package red_social_academica.red_social_academica.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

/**
 * Entidad que representa un rol del sistema (ej: ROLE_ADMIN, ROLE_PUBLIC).
 */

@Entity
@Table(name = "role")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING) // Almacena el nombre del rol como una cadena en la base de datos
    @Column(length = 20) // Longitud máxima de 20 caracteres para el nombre del rol
    private NombreRol nombre;
    
    public enum NombreRol { // Enum para definir los nombres de los roles
        ROLE_PUBLIC,
        ROLE_ADMIN
    }
}
