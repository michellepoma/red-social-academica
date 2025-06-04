package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.notification.NotificationDTO;
import red_social_academica.red_social_academica.service.INotificationService;
import red_social_academica.red_social_academica.auth.security.AuthUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del usuario")
public class NotificationController {

    private final INotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Listar todas las notificaciones activas del usuario autenticado")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> obtenerNotificaciones() {
        long inicio = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Inicio obtenerNotificaciones: {}", inicio);

        String username = AuthUtils.getCurrentUsername();
        List<NotificationDTO> lista = notificationService.obtenerTodas(username);

        long fin = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Fin obtenerNotificaciones: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener las 10 notificaciones no leídas más recientes")
    @GetMapping("/no-leidas/top")
    public ResponseEntity<List<NotificationDTO>> ultimas10NoLeidas() {
        long inicio = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Inicio ultimas10NoLeidas: {}", inicio);

        String username = AuthUtils.getCurrentUsername();
        List<NotificationDTO> lista = notificationService.obtenerUltimasNoLeidas(username);

        long fin = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Fin ultimas10NoLeidas: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Contar notificaciones no leídas")
    @GetMapping("/no-leidas/count")
    public ResponseEntity<Long> contarNoLeidas() {
        long inicio = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Inicio contarNoLeidas: {}", inicio);

        String username = AuthUtils.getCurrentUsername();
        Long count = notificationService.contarNoLeidas(username);

        long fin = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Fin contarNoLeidas: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Marcar una notificación como leída")
    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificationDTO> marcarComoLeida(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Inicio marcarComoLeida: {}", inicio);

        String username = AuthUtils.getCurrentUsername();
        NotificationDTO dto = notificationService.marcarComoLeida(id, username);

        long fin = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Fin marcarComoLeida: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Eliminar una notificación (lógicamente)")
    @PutMapping("/{id}/baja")
    public ResponseEntity<NotificationDTO> eliminarNotificacion(@PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Inicio eliminarNotificacion: {}", inicio);

        String username = AuthUtils.getCurrentUsername();
        NotificationDTO dto = notificationService.eliminarNotificacion(id, username);

        long fin = System.currentTimeMillis();
        logger.info("[NOTIFICACION] Fin eliminarNotificacion: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(dto);
    }
}
