package red_social_academica.red_social_academica.dto.user;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * DTO para crear un nuevo usuario desde el panel de administrador.
 * Hereda validaciones de UserCreateDTO y agrega campo de rol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@Schema(description = "DTO para crear un nuevo usuario desde el panel de administrador")

public class UserCreateAdminDTO extends UserCreateDTO implements Serializable {

    @Schema(
        description = "Rol que se asignará al nuevo usuario",
        example = "ROLE_ADMIN",
        allowableValues = {"ROLE_ADMIN", "ROLE_PUBLIC"}  // Agrega aquí todos los roles válidos
    )
    @NotBlank(message = "El rol es obligatorio para creación desde el admin")
    private String rol;
}