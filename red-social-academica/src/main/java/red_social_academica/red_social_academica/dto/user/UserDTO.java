package red_social_academica.red_social_academica.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO que representa los datos públicos del usuario para transferencia entre capas.
 * Incluye validaciones básicas y anotaciones para documentación OpenAPI.
 * 
 * Excluye campos sensibles como contraseña y relaciones complejas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar el perfil público de un usuario")
public class UserDTO implements Serializable {

    @Schema(description = "Identificador único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Correo electrónico del usuario", example = "ana.gomez@universidad.edu")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Schema(description = "Nombre de usuario único", example = "ana_gomez")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    private String username;

    @Schema(description = "Registro universitario unico", example = "1822345")
    @NotBlank(message = "El registro universitario es obligatorio")
    @Size(min=7, message = "El registro universitario debe tener minimo 7 numeros")
    @Column(unique = true, nullable = false)
    private String ru;

    @Schema(description = "Nombre del usuario", example = "Ana")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Gómez")
    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @Schema(description = "URL de la foto de perfil del usuario", example = "https://cdn.misitio.com/perfiles/ana.jpg")
    @Size(max = 255, message = "La URL no debe superar los 255 caracteres")
    private String profilePictureUrl;

    @Schema(description = "Biografía o descripción corta del usuario", example = "Estudiante de ingeniería apasionada por la IA")
    @Size(max = 300, message = "La biografía no debe superar los 300 caracteres")
    private String bio;

    @Schema(description = "Carrera o especialización del usuario", example = "Ingeniería en Sistemas")
    @Size(max = 100, message = "La carrera no debe superar los 100 caracteres")
    private String career;

    @Schema(description = "Fecha de nacimiento del usuario", example = "2002-05-14")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate birthdate;

    @Schema(description = "Usuario que creó este registro", example = "ana_gomez")
    private String usuarioAlta;

    @Schema(description = "Fecha de creación del perfil", example = "2025-05-10", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaAlta;

    @Schema(description = "Última fecha de modificación del perfil", example = "2025-05-20", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaModificacion;

    @Schema(description = "Motivo de desactivación o baja del usuario", example = "actividad sospechosa", accessMode = Schema.AccessMode.READ_ONLY)
    private String motivoBaja;

    @Schema(description = "Fecha en la que se dio de baja el usuario", example = "2025-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaBaja;

    @Schema(description = "Indica si el usuario está activo o ha sido dado de baja", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo;

    @Schema(description = "Roles asignados al usuario", example = "[\"ROLE_PUBLIC\"]")
    private List<String> roles;
}