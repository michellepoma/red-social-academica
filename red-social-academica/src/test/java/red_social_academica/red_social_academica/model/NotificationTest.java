package red_social_academica.red_social_academica.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationTest {

    @Test
    void construirNotification_conValoresBasicos_funciona() {
        User usuario = User.builder()
                .id(5L)
                .username("laura")
                .name("Laura")
                .lastName("Ramos")
                .build();

        Date ahora = new Date();

        Notification n = Notification.builder()
                .id(10L)
                .message("Notificación de prueba")
                .read(false)
                .createdAt(ahora)
                .targetUrl("/post/22")
                .recipient(usuario)
                .activo(true)
                .build();

        assertThat(n.getId()).isEqualTo(10L);
        assertThat(n.getMessage()).isEqualTo("Notificación de prueba");
        assertThat(n.isRead()).isFalse();
        assertThat(n.getCreatedAt()).isEqualTo(ahora);
        assertThat(n.getTargetUrl()).isEqualTo("/post/22");
        assertThat(n.getRecipient()).isEqualTo(usuario);
        assertThat(n.isActivo()).isTrue();
    }
}