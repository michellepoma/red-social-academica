package red_social_academica.red_social_academica.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import red_social_academica.red_social_academica.model.Notification;
import red_social_academica.red_social_academica.model.User;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class NotificationRepositoryIntegrationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debe guardar y recuperar notificaciones de un usuario")
    void guardarYRecuperarNotificaciones() {
        User user = User.builder()
                .username("ana")
                .email("ana@uni.edu")
                .name("Ana")
                .lastName("Gómez")
                .ru("1234567")
                .password("clave123")
                .activo(true)
                .build();
        userRepository.save(user);

        Notification n = Notification.builder()
                .message("Nuevo comentario")
                .read(false)
                .createdAt(new Date())
                .recipient(user)
                .activo(true)
                .build();
        notificationRepository.save(n);

        List<Notification> notificaciones = notificationRepository
                .findByRecipientUsernameAndActivoTrueOrderByCreatedAtDesc("ana");

        assertThat(notificaciones).isNotEmpty();
        assertThat(notificaciones.get(0).getMessage()).isEqualTo("Nuevo comentario");
    }
}