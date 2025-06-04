package red_social_academica.red_social_academica.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import red_social_academica.red_social_academica.dto.invitation.*;
import red_social_academica.red_social_academica.service.IInvitationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/invitaciones")
@Tag(name = "Invitaciones", description = "Gestión de solicitudes de amistad entre usuarios")
public class InvitationController {

    private final IInvitationService invitationService;
    private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);

    @Autowired
    public InvitationController(IInvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @Operation(summary = "Enviar una invitación de amistad")
    @PostMapping("/{senderUsername}")
    public ResponseEntity<InvitationDTO> enviarInvitacion(
            @PathVariable String senderUsername,
            @Valid @RequestBody InvitationCreateDTO dto) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio enviarInvitacion: {}", inicio);

        InvitationDTO nueva = invitationService.enviarInvitacion(senderUsername, dto);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin enviarInvitacion: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Aceptar una invitación")
    @PutMapping("/{invitationId}/aceptar")
    public ResponseEntity<InvitationDTO> aceptarInvitacion(
            @PathVariable Long invitationId,
            @RequestHeader("username") String receiverUsername) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio aceptarInvitacion: {}", inicio);

        InvitationDTO dto = invitationService.aceptarInvitacion(invitationId, receiverUsername);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin aceptarInvitacion: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Rechazar una invitación")
    @PutMapping("/{invitationId}/rechazar")
    public ResponseEntity<InvitationDTO> rechazarInvitacion(
            @PathVariable Long invitationId,
            @RequestHeader("username") String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio rechazarInvitacion: {}", inicio);

        InvitationDTO dto = invitationService.rechazarInvitacion(invitationId, username);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin rechazarInvitacion: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Obtener invitaciones recibidas por un usuario")
    @GetMapping("/recibidas/{username}")
    public ResponseEntity<List<InvitationDTO>> obtenerRecibidas(@PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio obtenerRecibidas: {}", inicio);

        List<InvitationDTO> lista = invitationService.obtenerInvitacionesRecibidas(username);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin obtenerRecibidas: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener invitaciones enviadas por un usuario")
    @GetMapping("/enviadas/{username}")
    public ResponseEntity<List<InvitationDTO>> obtenerEnviadas(@PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio obtenerEnviadas: {}", inicio);

        List<InvitationDTO> lista = invitationService.obtenerInvitacionesEnviadas(username);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin obtenerEnviadas: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Cancelar una invitación enviada (por el remitente)")
    @PutMapping("/{invitationId}/cancelar")
    public ResponseEntity<InvitationDTO> cancelarInvitacion(
            @PathVariable Long invitationId,
            @RequestHeader("username") String senderUsername) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio cancelarInvitacion: {}", inicio);

        InvitationDTO dto = invitationService.cancelarInvitacion(invitationId, senderUsername);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin cancelarInvitacion: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Obtener invitaciones pendientes recibidas (aún no aceptadas/rechazadas)")
    @GetMapping("/pendientes/recibidas/{username}")
    public ResponseEntity<List<InvitationDTO>> obtenerPendientesRecibidas(
            @PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio obtenerPendientesRecibidas: {}", inicio);

        List<InvitationDTO> lista = invitationService.obtenerInvitacionesPendientesRecibidas(username);

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin obtenerPendientesRecibidas: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener todas las invitaciones activas (admin)")
    @GetMapping("/activas")
    public ResponseEntity<List<InvitationDTO>> obtenerTodasActivas(
            @RequestHeader("role") String role) {
        long inicio = System.currentTimeMillis();
        logger.info("[INVITACION] Inicio obtenerTodasActivas: {}", inicio);

        if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
            logger.warn("[INVITACION] Acceso denegado para obtenerTodasActivas con rol: {}", role);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<InvitationDTO> lista = invitationService.obtenerTodasInvitacionesActivas();

        long fin = System.currentTimeMillis();
        logger.info("[INVITACION] Fin obtenerTodasActivas: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(lista);
    }
}
