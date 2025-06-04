package red_social_academica.red_social_academica.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO para actualizar una publicación existente.
 * Todos los campos son opcionales, pero deben validarse si se proporcionan.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para actualizar el contenido de una publicación")
public class PostUpdateDTO implements Serializable {

    @Schema(description = "Título actualizado", example = "Actualización del taller de Git")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    private String title;

    @Schema(description = "Contenido actualizado del post", example = "Se agregó el enlace a los ejercicios.")
    @Size(max = 1000, message = "El contenido no puede superar los 1000 caracteres")
    private String text;

    @Schema(description = "Nueva imagen adjunta", example = "https://miapp.com/img/post-actualizado.jpg")
    @Size(max = 255, message = "La URL de la imagen no puede superar los 255 caracteres")
    private String imageUrl;

    @Schema(description = "Nuevo recurso adjunto", example = "https://drive.google.com/nuevo_apunte.pdf")
    @Size(max = 255, message = "La URL del recurso no puede superar los 255 caracteres")
    private String resourceUrl;

    @Schema(description = "Fecha actualizada del evento", example = "2025-06-20")
    private LocalDate eventDate;
}
