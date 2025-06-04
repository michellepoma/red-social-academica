package red_social_academica.red_social_academica.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO para la creación de nuevas publicaciones por parte de los usuarios.
 * Todos los campos adicionales (imagen, recurso, evento) son opcionales.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para crear una nueva publicación")
public class PostCreateDTO implements Serializable {

    @Schema(description = "Título de la publicación", example = "Taller de Git y GitHub")
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String title;

    @Schema(description = "Contenido del post", example = "Aquí están los apuntes del taller de Git.")
    @Size(max = 1000, message = "El contenido no puede superar los 1000 caracteres")
    private String text;

    @Schema(description = "URL de una imagen adjunta", example = "https://miapp.com/img/post1.jpg")
    @Size(max = 255, message = "La URL de la imagen no puede superar los 255 caracteres")
    private String imageUrl;

    @Schema(description = "URL de un recurso adjunto", example = "https://drive.google.com/apuntes.pdf")
    @Size(max = 255, message = "La URL del recurso no puede superar los 255 caracteres")
    private String resourceUrl;

    @Schema(description = "Fecha del evento, si aplica", example = "2025-06-15")
    private LocalDate eventDate;
}