package red_social_academica.red_social_academica.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import red_social_academica.red_social_academica.dto.invitation.InvitationCreateDTO;
import red_social_academica.red_social_academica.dto.invitation.InvitationDTO;
import red_social_academica.red_social_academica.model.Invitation;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.InvitationRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.impl.InvitationServiceImpl;
import red_social_academica.red_social_academica.validation.InvitationValidator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InvitationServiceImplTest {

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InvitationValidator invitationValidator;

    @Mock
    private INotificationService notificationService;

    @InjectMocks
    private InvitationServiceImpl invitationService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sender = User.builder().id(1L).username("juan").name("Juan").lastName("Perez").email("juan@ex.com")
                .ru("1234567").password("123456").build();
        receiver = User.builder().id(2L).username("ana").name("Ana").lastName("Lopez").email("ana@ex.com").ru("7654321")
                .password("654321").build();
    }

    @Test
    void testEnviarInvitacion() {
        InvitationCreateDTO dto = new InvitationCreateDTO("ana");

        when(userRepository.findByUsernameAndActivoTrue("juan")).thenReturn(Optional.of(sender));
        when(userRepository.findByUsernameAndActivoTrue("ana")).thenReturn(Optional.of(receiver));
        when(invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue("juan", "ana"))
                .thenReturn(false);

        Invitation saved = Invitation.builder().id(100L).sender(sender).receiver(receiver).activo(true).build();
        when(invitationRepository.save(any())).thenReturn(saved);

        when(notificationService.crearNotificacion(anyString(), anyString(), anyString()))
                .thenReturn(null); // ✅ arreglo aquí

        InvitationDTO result = invitationService.enviarInvitacion("juan", dto);

        assertThat(result.getSenderId()).isEqualTo(1L);
        assertThat(result.getReceiverId()).isEqualTo(2L);
    }

    @Test
    void testAceptarInvitacion() {
        Invitation invitation = Invitation.builder().id(100L).sender(sender).receiver(receiver).activo(true).build();
        when(invitationRepository.findById(100L)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(any())).thenReturn(invitation);

        when(notificationService.crearNotificacion(anyString(), anyString(), anyString()))
                .thenReturn(null); // ✅ arreglo aquí

        InvitationDTO result = invitationService.aceptarInvitacion(100L, "ana");

        assertThat(result.isActivo()).isFalse();
        assertThat(result.getMotivoBaja()).isEqualTo("aceptada");
    }

    @Test
    void testRechazarInvitacion() {
        Invitation invitation = Invitation.builder().id(101L).sender(sender).receiver(receiver).activo(true).build();
        when(invitationRepository.findById(101L)).thenReturn(Optional.of(invitation));
        when(invitationRepository.save(any())).thenReturn(invitation);

        InvitationDTO result = invitationService.rechazarInvitacion(101L, "juan");

        assertThat(result.isActivo()).isFalse();
        assertThat(result.getMotivoBaja()).isEqualTo("rechazada");
    }

    @Test
    void testYaExisteInvitacion() {
        when(invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue("juan", "ana"))
                .thenReturn(true);
        boolean result = invitationService.yaExisteInvitacion("juan", "ana");
        assertThat(result).isTrue();
    }
}
