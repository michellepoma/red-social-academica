package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.invitation.*;

import java.util.List;

public interface IInvitationService {

    /**
     * Envía una nueva invitación de amistad si no existe otra activa.
     * 
     * @param senderUsername nombre de usuario del remitente
     * @param dto            datos del destinatario
     * @return Invitación creada
     */
    InvitationDTO enviarInvitacion(String senderUsername, InvitationCreateDTO dto);

    /**
     * Devuelve las invitaciones activas recibidas por un usuario.
     * 
     * @param username nombre de usuario que recibe
     * @return Lista de invitaciones recibidas
     */
    List<InvitationDTO> obtenerInvitacionesRecibidas(String username);

    /**
     * Devuelve las invitaciones activas enviadas por un usuario.
     * 
     * @param username nombre de usuario que envió
     * @return Lista de invitaciones enviadas
     */
    List<InvitationDTO> obtenerInvitacionesEnviadas(String username);

    /**
     * Devuelve todas las invitaciones activas del sistema (solo para ADMIN).
     * 
     * @return Lista global de invitaciones activas
     */
    List<InvitationDTO> obtenerTodasInvitacionesActivas();

    /**
     * Acepta una invitación, crea relación de amistad, y la desactiva.
     * 
     * @param invitationId     ID de la invitación
     * @param receiverUsername nombre de usuario del que acepta
     * @return Invitación aceptada (y cerrada)
     */
    InvitationDTO aceptarInvitacion(Long invitationId, String receiverUsername);

    /**
     * Rechaza o cancela una invitación activa.
     * 
     * @param invitationId ID de la invitación
     * @param username     usuario que rechaza (debe ser parte de la invitación)
     * @return Invitación rechazada
     */
    InvitationDTO rechazarInvitacion(Long invitationId, String username);

    /**
     * Verifica si ya existe una invitación activa entre dos usuarios.
     * 
     * @param senderUsername   remitente
     * @param receiverUsername destinatario
     * @return true si ya existe
     */
    boolean yaExisteInvitacion(String senderUsername, String receiverUsername);

    InvitationDTO cancelarInvitacion(Long invitationId, String senderUsername);

    List<InvitationDTO> obtenerInvitacionesPendientesRecibidas(String username);

    InvitationDTO obtenerDetalleInvitacion(Long invitationId, String username, String role);

}
