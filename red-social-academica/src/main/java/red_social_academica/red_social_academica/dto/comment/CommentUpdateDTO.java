package red_social_academica.red_social_academica.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

/**
 * DTO para actualizar un nuevo comentario en una publicación (solo autor y admin).
 * 
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "DTO para actualizar un comentario existente")
public class CommentUpdateDTO implements Serializable {

    @Schema(description = "Nuevo contenido del comentario", example = "Actualizado: excelente aporte.")
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(min = 2, max = 500, message = "El contenido debe tener entre 2 y 500 caracteres")
    private String content;
}
