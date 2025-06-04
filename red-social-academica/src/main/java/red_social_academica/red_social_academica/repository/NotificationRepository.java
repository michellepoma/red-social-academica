package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notificaciones activas para un usuario ordenadas por fecha
    List<Notification> findByRecipientUsernameAndActivoTrueOrderByCreatedAtDesc(String username);

    // Contar notificaciones activas no leídas
    long countByRecipientUsernameAndReadFalseAndActivoTrue(String username);

    // Últimas 10 notificaciones activas para el usuario
    List<Notification> findTop10ByRecipientUsernameAndActivoTrueOrderByCreatedAtDesc(String username);

    // Obtener por ID solo si está activa
    Optional<Notification> findByIdAndActivoTrue(Long id);
}
