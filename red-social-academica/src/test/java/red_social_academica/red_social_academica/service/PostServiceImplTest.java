package red_social_academica.red_social_academica.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import red_social_academica.red_social_academica.dto.post.PostCreateDTO;
import red_social_academica.red_social_academica.dto.post.PostDTO;
import red_social_academica.red_social_academica.model.Post;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.PostRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.impl.PostServiceImpl;
import red_social_academica.red_social_academica.validation.PostValidator;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostValidator postValidator;

    @InjectMocks
    private PostServiceImpl postService;

    private User usuario;

    @BeforeEach
    void setUp() {
        usuario = User.builder()
                .id(1L)
                .username("juanito")
                .name("Juan")
                .lastName("Pérez")
                .activo(true)
                .build();
    }

    @Test
    void crearPost_valido_devuelvePostDTO() {
        PostCreateDTO dto = PostCreateDTO.builder()
                .title("Post de prueba")
                .text("Este es el contenido del post")
                .build();

        when(userRepository.findByUsernameAndActivoTrue("juanito")).thenReturn(Optional.of(usuario));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));
        doNothing().when(postValidator).validarCreacion(any());

        PostDTO resultado = postService.crearPost("juanito", dto);

        assertThat(resultado.getTitle()).isEqualTo("Post de prueba");
        assertThat(resultado.getAuthorFullName()).isEqualTo("Juan Pérez");
    }

    @Test
    void crearPost_usuarioNoEncontrado_lanzaExcepcion() {
        PostCreateDTO dto = PostCreateDTO.builder().title("Test").build();

        when(userRepository.findByUsernameAndActivoTrue("no_existe")).thenReturn(Optional.empty());
        doNothing().when(postValidator).validarCreacion(any());

        try {
            postService.crearPost("no_existe", dto);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Usuario no encontrado");
        }
    }
}