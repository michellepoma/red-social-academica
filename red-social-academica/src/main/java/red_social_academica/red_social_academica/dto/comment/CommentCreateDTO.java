package red_social_academica.red_social_academica.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

/**
 * DTO para crear un nuevo comentario en una publicación.
 * Contiene el ID del post y el contenido del comentario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear un nuevo comentario en una publicación")
public class CommentCreateDTO implements Serializable {

    @Schema(description = "ID del post al que se realiza el comentario", example = "101")
    @NotNull(message = "El ID del post es obligatorio")
    private Long postId;

    @Schema(description = "Contenido del comentario", example = "Muy interesante, gracias por compartir.")
    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(min = 2, max = 500, message = "El comentario debe tener entre 2 y 500 caracteres")
    private String content;
}