package red_social_academica.red_social_academica.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import red_social_academica.red_social_academica.model.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class InvitationRepositoryIntegrationTest {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setup() {
        sender = userRepository.save(User.builder().username("juan").name("Juan").lastName("Perez").email("juan@ex.com")
                .ru("1234567").password("123456").build());
        receiver = userRepository.save(User.builder().username("ana").name("Ana").lastName("Lopez").email("ana@ex.com")
                .ru("7654321").password("654321").build());
    }

    @Test
    void testGuardarYBuscarInvitacion() {
        Invitation invitation = Invitation.builder().sender(sender).receiver(receiver).activo(true).build();
        invitationRepository.save(invitation);

        List<Invitation> resultado = invitationRepository.findBySenderUsernameAndActivoTrue("juan");

        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getReceiver().getUsername()).isEqualTo("ana");
    }

    @Test
    void testExistsBySenderAndReceiver() {
        Invitation invitation = Invitation.builder().sender(sender).receiver(receiver).activo(true).build();
        invitationRepository.save(invitation);

        boolean existe = invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue("juan", "ana");

        assertThat(existe).isTrue();
    }
}