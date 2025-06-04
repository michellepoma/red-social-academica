package red_social_academica.red_social_academica.dto.registroUniversitario;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para la entidad RegistroUniversitario.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroUniversitarioDto implements Serializable {
    
    @Schema(description = "Identificador único del registro", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(
        description = "Registro Universitario (número de matrícula)",
        example = "1822305"
    )
    @NotBlank(message = "El RU no puede estar vacío")
    @Size(min = 6, max = 20, message = "El RU debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^\\d{6,20}$", message = "El RU debe contener solo dígitos")
    private String ru;
}
