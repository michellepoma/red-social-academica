package red_social_academica.red_social_academica.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO para la actualización del perfil de un usuario registrado.
 * No incluye cambios de contraseña ni email.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "DTO para actualizar los datos del perfil de un usuario")
public class UserUpdateDTO implements Serializable {

    @Schema(description = "Nombre actualizado del usuario", example = "Ana")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Schema(description = "Apellido actualizado del usuario", example = "Gómez")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Schema(description = "Username actualizado del usuario", example = "ana_g")
    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 3, max = 30, message = "El username debe tener entre 3 y 30 caracteres")
    private String username;

    @Schema(description = "Nueva biografía del usuario", example = "Estudiante de ingeniería en software")
    @Size(max = 300, message = "La biografía no debe superar los 300 caracteres")
    private String bio;

    @Schema(description = "Carrera académica actual", example = "Ingeniería de Sistemas")
    @Size(max = 100, message = "La carrera no debe superar los 100 caracteres")
    private String career;

    @Schema(description = "URL de la nueva foto de perfil", example = "https://cdn.miapp.com/perfiles/ana.jpg")
    @Size(max = 255, message = "La URL de la imagen no debe superar los 255 caracteres")
    private String profilePictureUrl;

    @Schema(description = "Fecha de nacimiento", example = "2001-10-12")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate birthdate;

    @Schema(description = "Correo electrónico del usuario", example = "ana.gomez@universidad.edu")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Schema(description = "Contraseña para el acceso", example = "segura123")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Confirmación de contraseña", example = "segura123")
    private String passwordConfirm;

}