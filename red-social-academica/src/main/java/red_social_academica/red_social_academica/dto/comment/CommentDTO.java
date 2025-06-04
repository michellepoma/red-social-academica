package red_social_academica.red_social_academica.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO que representa un comentario realizado por un usuario en una publicación.
 * Incluye información del contenido, fecha y autor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar un comentario en una publicación")
public class CommentDTO implements Serializable {

    @Schema(description = "Identificador único del comentario", example = "500", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Contenido del comentario", example = "¡Muy interesante, gracias por compartir!")
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(min = 2, max = 500, message = "El comentario debe tener entre 2 y 500 caracteres")
    private String content;

    @Schema(description = "Fecha de creación del comentario", example = "2025-05-24T20:15:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createdAt;

    @Schema(description = "ID del autor del comentario", example = "42")
    private Long authorId;

    @Schema(description = "Nombre completo del autor", example = "Carlos Fernández")
    private String authorFullName;

    @Schema(description = "ID de la publicación comentada", example = "101")
    private Long postId;

    @Schema(description = "Fecha de creación del comentario", example = "2025-05-10", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaAlta;

    @Schema(description = "Última fecha de modificación del comentario", example = "2025-05-20", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaModificacion;

    @Schema(description = "Motivo de baja del comentario (si fue eliminado)", example = "Contenido inapropiado", accessMode = Schema.AccessMode.READ_ONLY)
    private String motivoBaja;

    @Schema(description = "Fecha en la que se dio de baja el comentario", example = "2025-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaBaja;

    @Schema(description = "Indica si la publicacion esta activa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo;
}