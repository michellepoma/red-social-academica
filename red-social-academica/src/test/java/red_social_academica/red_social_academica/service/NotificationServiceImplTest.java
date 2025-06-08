package red_social_academica.red_social_academica.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import red_social_academica.red_social_academica.dto.notification.NotificationDTO;
import red_social_academica.red_social_academica.model.Notification;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.NotificationRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.impl.NotificationServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("carlos")
                .name("Carlos")
                .lastName("López")
                .activo(true)
                .build();
    }

    @Test
    void crearNotificacion_funciona() {
        when(userRepository.findByUsernameAndActivoTrue("carlos")).thenReturn(Optional.of(user));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificationDTO dto = notificationService.crearNotificacion("carlos", "Mensaje de prueba", "/link");
        assertThat(dto.getMessage()).isEqualTo("Mensaje de prueba");
        assertThat(dto.getRecipientName()).isEqualTo("Carlos López");
    }

    @Test
    void crearNotificacion_usuarioNoExiste_lanzaExcepcion() {
        when(userRepository.findByUsernameAndActivoTrue("desconocido")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> notificationService.crearNotificacion("desconocido", "Alerta", "/x"));

        assertThat(ex.getMessage()).contains("Usuario no encontrado");
    }

    @Test
    void marcarComoLeida_actualizaEstado() {
        Notification noti = Notification.builder()
                .id(1L)
                .message("Alerta")
                .read(false)
                .createdAt(new Date())
                .recipient(user)
                .activo(true)
                .build();

        when(notificationRepository.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(noti));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificationDTO dto = notificationService.marcarComoLeida(1L, "carlos");
        assertThat(dto.isRead()).isTrue();
    }

    @Test
    void eliminarNotificacion_funcionaCorrectamente() {
        Notification noti = Notification.builder()
                .id(2L)
                .message("Recordatorio")
                .read(false)
                .createdAt(new Date())
                .recipient(user)
                .activo(true)
                .build();

        when(notificationRepository.findByIdAndActivoTrue(2L)).thenReturn(Optional.of(noti));
        when(notificationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificationDTO dto = notificationService.eliminarNotificacion(2L, "carlos");
        assertThat(dto.isActivo()).isFalse();
    }
}