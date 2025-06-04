package red_social_academica.red_social_academica.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import red_social_academica.red_social_academica.dto.user.UserCreateAdminDTO;
import red_social_academica.red_social_academica.dto.user.UserDTO;
import red_social_academica.red_social_academica.auth.model.Role;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.auth.repository.RoleRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.validation.UserValidator;
import red_social_academica.red_social_academica.service.impl.UserServiceImpl;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void obtenerPorUsername_devuelveUsuarioDTO() {
        String username = "usuario123";
        User mockUser = User.builder()
                .username(username)
                .email("u@example.com")
                .name("U")
                .lastName("X")
                .build();

        // ✅ Rol válido simulado
        Role rol = new Role();
        rol.setNombre(Role.NombreRol.ROLE_PUBLIC);
        mockUser.setRoles(Set.of(rol));

        when(userRepository.findByUsernameAndActivoTrue(username)).thenReturn(Optional.of(mockUser));

        UserDTO dto = service.obtenerPorUsername(username);

        assertThat(dto.getUsername()).isEqualTo(username);
    }

    @Test
    void crearUsuarioComoAdmin_conContrasenasDistintas_lanzaExcepcion() {
        UserCreateAdminDTO dto = UserCreateAdminDTO.builder()
                .username("juanito")
                .password("123")
                .passwordConfirm("456")
                .rol("ROLE_PUBLIC")
                .build();

        assertThrows(BusinessException.class, () -> {
            service.crearUsuarioComoAdmin(dto);
        });
    }

    @Test
    void crearUsuarioComoAdmin_funcionaConDatosValidos() {
        // Simular el contexto de seguridad
        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);

        when(auth.getName()).thenReturn("admin");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        UserCreateAdminDTO dto = UserCreateAdminDTO.builder()
                .username("juanito")
                .email("j@x.com")
                .password("123")
                .passwordConfirm("123")
                .name("Juan")
                .lastName("Pérez")
                .ru("RU123")
                .rol("ROLE_PUBLIC")
                .build();

        Role rolMock = new Role();
        rolMock.setNombre(Role.NombreRol.ROLE_PUBLIC);

        when(passwordEncoder.encode("123")).thenReturn("hashed123");
        when(roleRepository.findByNombre(Role.NombreRol.ROLE_PUBLIC)).thenReturn(Optional.of(rolMock));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserDTO result = service.crearUsuarioComoAdmin(dto);

        assertThat(result.getUsername()).isEqualTo("juanito");
    }

    @Test
    void existePorUsername_retornaTrueSiExiste() {
        when(userRepository.findByUsernameAndActivoTrue("ana")).thenReturn(Optional.of(new User()));
        assertThat(service.existePorUsername("ana")).isTrue();
    }

    @Test
    void existePorUsername_retornaFalseSiNoExiste() {
        when(userRepository.findByUsernameAndActivoTrue("ana")).thenReturn(Optional.empty());
        assertThat(service.existePorUsername("ana")).isFalse();
    }
}
