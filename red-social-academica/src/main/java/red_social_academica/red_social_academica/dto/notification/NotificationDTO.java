package red_social_academica.red_social_academica.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO que representa una notificación enviada a un usuario.
 * Incluye el mensaje, estado de lectura, fecha de creación y destino del recurso relacionado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una notificación en el sistema")
public class NotificationDTO implements Serializable {

    @Schema(description = "Identificador único de la notificación", example = "301", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Mensaje de la notificación", example = "Juan comentó en tu publicación.")
    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no debe superar los 500 caracteres")
    private String message;

    @Schema(description = "Indica si la notificación ha sido leída", example = "false")
    private boolean read;

    @Schema(description = "Fecha de creación", example = "2025-05-24T21:10:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createdAt;

    @Schema(description = "Ruta destino asociada a la notificación", example = "/posts/101")
    @Size(max = 255, message = "La URL no debe superar los 255 caracteres")
    private String targetUrl;

    @Schema(description = "ID del destinatario", example = "15")
    private Long recipientId;

    @Schema(description = "Nombre del destinatario", example = "Ana Gómez")
    private String recipientName;

    @Schema(description = "Fecha de alta", example = "2025-05-24", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaAlta;

    @Schema(description = "Fecha de modificación", example = "2025-05-25", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaModificacion;

    @Schema(description = "Motivo de baja", example = "eliminada por el usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private String motivoBaja;

    @Schema(description = "Fecha de baja", example = "2025-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaBaja;

    @Schema(description = "Estado de la notificación", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo;
}
