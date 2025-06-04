package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.notification.*;

import java.util.List;

public interface INotificationService {

    /**
     * Crear una nueva notificacion para un usuario.
     * @param username destinatario
     * @param message mensaje a mostrar
     * @param targetUrl url de redireccion asociada
     * @return notificacion creada
     */
    NotificationDTO crearNotificacion(String username, String message, String targetUrl);

    /**
     * Marcar como leida una notificacion (solo el destinatario).
     * @param notificationId id
     * @param username autenticado
     * @return notificacion actualizada
     */
    NotificationDTO marcarComoLeida(Long notificationId, String username);

    /**
     * Eliminar lógicamente una notificacion (solo el destinatario).
     * @param notificationId id
     * @param username autenticado
     * @return notificacion eliminada
     */
    NotificationDTO eliminarNotificacion(Long notificationId, String username);

    /**
     * Listar notificaciones de un usuario
     * @param username usuario autenticado
     * @return lista ordenada por fecha
     */
    List<NotificationDTO> obtenerTodas(String username);

    /**
     * Obtener las 10 mas recientes no leidas
     * @param username usuario autenticado
     * @return top 10 notificaciones
     */
    List<NotificationDTO> obtenerUltimasNoLeidas(String username);

    /**
     * Contar cantidad de no leidas
     * @param username usuario
     * @return numero
     */
    long contarNoLeidas(String username);
}
