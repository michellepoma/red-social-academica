package red_social_academica.red_social_academica.dto.invitation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

/**
 * DTO para enviar una nueva invitación de amistad a otro usuario.
 * Solo necesita el ID o el email del destinatario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para enviar una invitacion de amistad")
public class InvitationCreateDTO implements Serializable {

    @Schema(description = "Username del usuario al que se desea enviar la invitacion", example = "ana_gomez")
    @NotBlank(message = "El username del destinatario es obligatorio")
    @Size(min = 3, max = 30, message = "El username debe tener entre 3 y 30 caracteres")
    private String receiverUsername;
}

