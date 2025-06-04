package red_social_academica.red_social_academica.dto.invitation;

import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO que representa una invitación de amistad entre dos usuarios.
 * Incluye información básica del remitente y destinatario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una invitacion de amistad")
public class InvitationDTO implements Serializable {

    @Schema(description = "ID unico de la invitacion", example = "200", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario que envia la invitacion", example = "10")
    private Long senderId;

    @Schema(description = "Nombre del remitente", example = "Carlos Ramirez")
    private String senderName;

    @Schema(description = "ID del usuario que recibe la invitacion", example = "25")
    private Long receiverId;

    @Schema(description = "Nombre del destinatario", example = "Ana Gomez")
    private String receiverName;

    @Schema(description = "Fecha de creacion de la invitacion", example = "2025-05-22", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaAlta;

    @Schema(description = "Fecha de la ultima modificacion de la invitacion", example = "2025-05-24", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaModificacion;

    @Schema(description = "Motivo de cancelacion o rechazo de la invitacion", example = "rechazada por el destinatario", accessMode = Schema.AccessMode.READ_ONLY)
    private String motivoBaja;

    @Schema(description = "Fecha de cancelacion o eliminacion de la invitacion", example = "2025-05-25", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate fechaBaja;

    @Schema(description = "Indica si la invitacion esta activa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean activo;
}
