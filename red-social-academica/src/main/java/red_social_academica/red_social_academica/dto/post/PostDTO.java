package red_social_academica.red_social_academica.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO para exponer publicaciones realizadas por los usuarios.
 * Permite representar publicaciones de tipo texto, recurso o evento, con campos opcionales.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una publicación en la red social académica")
public class PostDTO implements Serializable {

    @Schema(description = "Identificador único de la publicación", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Título de la publicación", example = "Conferencia sobre Inteligencia Artificial")
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String title;

    @Schema(description = "Texto o contenido principal del post", example = "Estaremos compartiendo los últimos avances en IA educativa.")
    @Size(max = 1000, message = "El contenido no puede superar los 1000 caracteres")
    private String text;

    @Schema(description = "Fecha y hora de publicación", example = "2025-05-24T18:45:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Date date;

    @Schema(description = "URL de una imagen adjunta", example = "https://cdn.misitio.com/images/evento.jpg")
    @Size(max = 255, message = "La URL de la imagen no debe superar los 255 caracteres")
    private String imageUrl;

    @Schema(description = "URL de un recurso adjunto (PDF, documento, etc.)", example = "https://drive.google.com/apuntesIA.pdf")
    @Size(max = 255, message = "La URL del recurso no debe superar los 255 caracteres")
    private String resourceUrl;

    @Schema(description = "Fecha del evento, si aplica", example = "2025-06-15")
    private LocalDate eventDate;

    @Schema(description = "Identificador del autor del post", example = "15")
    private Long userId;

    @Schema(description = "Nombre del autor del post", example = "Ana Gómez")
    private String authorFullName;

    @Schema(description = "Fecha de creación del post", example = "2025-05-24", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaAlta;

    @Schema(description = "Fecha de la última modificación", example = "2025-05-25", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaModificacion;

    @Schema(description = "Motivo de baja del post (si aplica)", example = "inapropiado", accessMode = Schema.AccessMode.READ_ONLY)
    private String motivoBaja;

    @Schema(description = "Fecha en que el post fue dado de baja (si aplica)", example = "2025-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaBaja;

    @Schema(description = "Indica si la publicacion esta activa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo;

}
