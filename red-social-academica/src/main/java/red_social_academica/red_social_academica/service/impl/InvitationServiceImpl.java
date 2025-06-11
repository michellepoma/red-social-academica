package red_social_academica.red_social_academica.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import red_social_academica.red_social_academica.dto.invitation.InvitationCreateDTO;
import red_social_academica.red_social_academica.dto.invitation.InvitationDTO;
import red_social_academica.red_social_academica.model.Invitation;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.InvitationRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IInvitationService;
import red_social_academica.red_social_academica.service.INotificationService;
import red_social_academica.red_social_academica.validation.InvitationValidator;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

@Service
public class InvitationServiceImpl implements IInvitationService {

        @Autowired
        private InvitationRepository invitationRepository;

        @Autowired
        private INotificationService notificationService;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private InvitationValidator invitationValidator;

        @Override
        @Transactional
        @Caching(evict = {
                        @CacheEvict(value = "invitacionesEnviadas", key = "#senderUsername"),
                        @CacheEvict(value = "invitacionesPendientes", key = "#dto.receiverUsername"),
                        @CacheEvict(value = "invitacionesRecibidas", key = "#dto.receiverUsername"),
                        @CacheEvict(value = "todasInvitacionesActivas", allEntries = true)
        })
        public InvitationDTO enviarInvitacion(String senderUsername, InvitationCreateDTO dto) {

                invitationValidator.validarEnvio(senderUsername, dto.getReceiverUsername());

                User sender = userRepository.findByUsernameAndActivoTrue(senderUsername)
                                .orElseThrow(() -> new RuntimeException("Remitente no encontrado o inactivo"));

                User receiver = userRepository.findByUsernameAndActivoTrue(dto.getReceiverUsername())
                                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado o inactivo"));

                if (invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue(senderUsername,
                                dto.getReceiverUsername())) {
                        throw new IllegalStateException("Ya existe una invitacion activa entre estos usuarios");
                }

                Invitation invitation = Invitation.builder()
                                .sender(sender)
                                .receiver(receiver)
                                .fechaAlta(LocalDate.now())
                                .usuarioAlta(senderUsername)
                                .activo(true)
                                .build();
                notificationService.crearNotificacion(
                                receiver.getUsername(),
                                sender.getUsername() + " te ha enviado una solicitud de amistad",
                                "/usuario/invitaciones");

                return convertToDTO(invitationRepository.save(invitation));
        }

        @Override
        @Transactional
        @Caching(evict = {
                        @CacheEvict(value = "invitacionesEnviadas", key = "#result.senderUsername"),
                        @CacheEvict(value = "invitacionesPendientes", key = "#receiverUsername"),
                        @CacheEvict(value = "invitacionesRecibidas", key = "#receiverUsername"),
                        @CacheEvict(value = "friendsCache", allEntries = true),
                        @CacheEvict(value = "todasInvitacionesActivas", allEntries = true)
        })
        public InvitationDTO aceptarInvitacion(Long invitationId, String receiverUsername) {
                Invitation invitation = invitationRepository.findById(invitationId)
                                .orElseThrow(() -> new BusinessException("Invitación no encontrada"));

                if (!invitation.isActivo()) {
                        throw new BusinessException("La invitación ya fue procesada (aceptada, rechazada o cancelada)");
                }

                if (!invitation.getReceiver().getUsername().equals(receiverUsername)) {
                        throw new SecurityException("No tienes permiso para aceptar esta invitación");
                }

                String senderUsername = invitation.getSender().getUsername(); // CAPTURA ANTES

                // Aceptar invitación
                invitation.accept();
                invitation.setActivo(false);
                invitation.setFechaBaja(LocalDate.now());
                invitation.setUsuarioBaja(receiverUsername);
                invitation.setMotivoBaja("aceptada");

                // Guardar la amistad
                userRepository.save(invitation.getSender());
                userRepository.save(invitation.getReceiver());

                notificationService.crearNotificacion(
                                senderUsername,
                                receiverUsername + " aceptó tu solicitud de amistad, ahora son amigos!",
                                "/usuario/amigos");

                return convertToDTO(invitationRepository.save(invitation));
        }

        @Override
        @Transactional
        @Caching(evict = {
                        @CacheEvict(value = "invitacionesEnviadas", key = "#result.senderUsername"),
                        @CacheEvict(value = "invitacionesPendientes", key = "#username"),
                        @CacheEvict(value = "invitacionesRecibidas", key = "#username"),
                        @CacheEvict(value = "todasInvitacionesActivas", allEntries = true)
        })
        public InvitationDTO rechazarInvitacion(Long invitationId, String username) {
                Invitation invitation = invitationRepository.findById(invitationId)
                                .filter(Invitation::isActivo)
                                .orElseThrow(() -> new RuntimeException("Invitación no encontrada o inactiva"));

                if (!invitation.esDelUsuario(username)) {
                        throw new SecurityException("No tienes permiso para rechazar esta invitación");
                }

                invitation.setActivo(false);
                invitation.setFechaBaja(LocalDate.now());
                invitation.setUsuarioBaja(username);
                invitation.setMotivoBaja("rechazada");

                return convertToDTO(invitationRepository.save(invitation));
        }

        @Override
        @Cacheable(value = "invitacionesEnviadas", key = "#username")
        public List<InvitationDTO> obtenerInvitacionesEnviadas(String username) {
                return invitationRepository.findAllByActivoTrue().stream()
                                .filter(inv -> inv.getSender().getUsername().equals(username))
                                .map(this::convertToDTO)
                                .toList();
        }

        @Override
        @Cacheable(value = "invitacionesRecibidas", key = "#username")
        public List<InvitationDTO> obtenerInvitacionesRecibidas(String username) {
                System.out.println("✅ Cargando invitaciones recibidas desde la BD...");

                return invitationRepository.findByReceiver_UsernameAndActivoTrue(username).stream()
                                .map(this::convertToDTO)
                                .toList();
        }

        @Override
        @Cacheable(value = "todasInvitacionesActivas")
        public List<InvitationDTO> obtenerTodasInvitacionesActivas() {
                return invitationRepository.findAllByActivoTrue().stream()
                                .map(this::convertToDTO)
                                .toList();
        }

        @Override
        @Transactional
        @Caching(evict = {
                        @CacheEvict(value = "invitacionesEnviadas", key = "#senderUsername"),
                        @CacheEvict(value = "invitacionesRecibidas", key = "#result.receiverUsername"),
                        @CacheEvict(value = "todasInvitacionesActivas", allEntries = true)
        })
        public InvitationDTO cancelarInvitacion(Long invitationId, String senderUsername) {
                Invitation invitation = invitationRepository.findById(invitationId)
                                .filter(Invitation::isActivo)
                                .orElseThrow(() -> new RuntimeException("Invitación no encontrada o inactiva"));

                if (!invitation.getSender().getUsername().equals(senderUsername)) {
                        throw new SecurityException("No tienes permiso para cancelar esta invitación");
                }

                String receiverUsername = invitation.getReceiver().getUsername();

                invitation.setActivo(false);
                invitation.setFechaBaja(LocalDate.now());
                invitation.setUsuarioBaja(senderUsername);
                invitation.setMotivoBaja("cancelada por el remitente");

                return convertToDTO(invitationRepository.save(invitation));
        }

        @Override
        @Cacheable(value = "invitacionesPendientes", key = "#username")
        public List<InvitationDTO> obtenerInvitacionesPendientesRecibidas(String username) {
                return invitationRepository.findByReceiver_UsernameAndActivoTrue(username).stream()
                                .map(this::convertToDTO)
                                .toList();
        }

        @Override
        @Cacheable(value = "detalleInvitacion", key = "#invitationId")
        public InvitationDTO obtenerDetalleInvitacion(Long invitationId, String username, String role) {
                Invitation invitation = invitationRepository.findById(invitationId)
                                .orElseThrow(() -> new RuntimeException("Invitacion no encontrada"));

                boolean esAdmin = "ROLE_ADMIN".equalsIgnoreCase(role);
                boolean esParticipante = invitation.getSender().getUsername().equals(username)
                                || invitation.getReceiver().getUsername().equals(username);

                if (!esAdmin && !esParticipante) {
                        throw new SecurityException("No tienes permisos para ver esta invitacion");
                }

                return convertToDTO(invitation);
        }

        @Override
        @Cacheable(value = "invitacionesExistentes", key = "#senderUsername + '-' + #receiverUsername")
        public boolean yaExisteInvitacion(String senderUsername, String receiverUsername) {
                return invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue(senderUsername,
                                receiverUsername);
        }

        // ======================= MAPEOS =======================

        private InvitationDTO convertToDTO(Invitation inv) {
                User sender = inv.getSender();
                User receiver = inv.getReceiver();

                return InvitationDTO.builder()
                                .id(inv.getId())
                                .senderId(sender != null ? sender.getId() : null)
                                .senderName(sender != null ? sender.getName() + " " + sender.getLastName() : null)
                                .senderUsername(sender != null ? sender.getUsername() : null) // ← AÑADIDO AQUÍ
                                .receiverId(receiver != null ? receiver.getId() : null)
                                .receiverName(receiver != null ? receiver.getName() + " " + receiver.getLastName()
                                                : null)
                                .receiverUsername(receiver != null ? receiver.getUsername() : null) //
                                .fechaAlta(inv.getFechaAlta())
                                .fechaModificacion(inv.getFechaModificacion())
                                .fechaBaja(inv.getFechaBaja())
                                .motivoBaja(inv.getMotivoBaja())
                                .activo(inv.isActivo())
                                .build();
        }

}
