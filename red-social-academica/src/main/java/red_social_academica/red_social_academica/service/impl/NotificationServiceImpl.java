package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.dto.notification.NotificationDTO;
import red_social_academica.red_social_academica.model.Notification;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.NotificationRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.INotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    @CacheEvict(value = "notificaciones", key = "#username") // limpia la cache cuando se crea
    public NotificationDTO crearNotificacion(String username, String message, String targetUrl) {
        User recipient = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        Notification notification = Notification.builder()
                .message(message)
                .targetUrl(targetUrl)
                .read(false)
                .createdAt(new Date())
                .fechaAlta(LocalDate.now())
                .usuarioAlta("system")
                .activo(true)
                .recipient(recipient)
                .build();

        return convertToDTO(notificationRepository.save(notification));
    }

    @Override
    @Cacheable(value = "notificaciones", key = "#username")
    public List<NotificationDTO> obtenerTodas(String username) {
        return notificationRepository.findByRecipientUsernameAndActivoTrueOrderByCreatedAtDesc(username)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public long contarNoLeidas(String username) {
        return notificationRepository.countByRecipientUsernameAndReadFalseAndActivoTrue(username);
    }

    @Override
    public List<NotificationDTO> obtenerUltimasNoLeidas(String username) {
        return notificationRepository.findByRecipientUsernameAndActivoTrueOrderByCreatedAtDesc(username)
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(value = "notificaciones", key = "#username") // actualiza la cache del usuario
    public NotificationDTO marcarComoLeida(Long id, String username) {
        Notification notification = notificationRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada o inactiva"));

        if (!notification.getRecipient().getUsername().equals(username)) {
            throw new SecurityException("No puedes modificar esta notificacion");
        }

        notification.setRead(true);
        notification.setFechaModificacion(LocalDate.now());
        notification.setUsuarioModificacion(username);
        return convertToDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    @CacheEvict(value = "notificaciones", key = "#username") // elimina la cache del usuario
    public NotificationDTO eliminarNotificacion(Long id, String username) {
        Notification notification = notificationRepository.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada o ya eliminada"));

        if (!notification.getRecipient().getUsername().equals(username)) {
            throw new SecurityException("No tienes permisos para eliminar esta notificacion");
        }

        notification.setActivo(false);
        notification.setFechaBaja(LocalDate.now());
        notification.setUsuarioBaja(username);
        notification.setMotivoBaja("eliminada por el usuario");

        return convertToDTO(notificationRepository.save(notification));
    }

    private NotificationDTO convertToDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .message(n.getMessage())
                .read(n.isRead())
                .createdAt(n.getCreatedAt())
                .targetUrl(n.getTargetUrl())
                .recipientId(n.getRecipient().getId())
                .recipientName(n.getRecipient().getName() + " " + n.getRecipient().getLastName())
                .build();
    }
}
