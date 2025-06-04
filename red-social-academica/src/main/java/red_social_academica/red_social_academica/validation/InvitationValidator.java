package red_social_academica.red_social_academica.validation;

import org.springframework.stereotype.Component;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.InvitationRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

@Component
public class InvitationValidator {

    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;

    public InvitationValidator(InvitationRepository invitationRepository, UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Valida que se pueda enviar una invitación entre dos usuarios.
     *
     * @param senderUsername    usuario que envía
     * @param receiverUsername  usuario que recibe
     */
    public void validarEnvio(String senderUsername, String receiverUsername) {

        if (senderUsername.equalsIgnoreCase(receiverUsername)) {
            throw new BusinessException("No puedes enviarte una invitación a ti mismo.");
        }

        User sender = userRepository.findByUsernameAndActivoTrue(senderUsername)
                .orElseThrow(() -> new BusinessException("El remitente no existe o está inactivo."));

        User receiver = userRepository.findByUsernameAndActivoTrue(receiverUsername)
                .orElseThrow(() -> new BusinessException("El destinatario no existe o está inactivo."));

        if (invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue(senderUsername, receiverUsername)) {
            throw new BusinessException("Ya existe una invitación activa entre estos usuarios.");
        }

        if (sender.getFriends().contains(receiver)) {
            throw new BusinessException("Ya eres amigo de este usuario.");
        }
    }
}