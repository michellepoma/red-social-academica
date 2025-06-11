package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.comment.*;
import red_social_academica.red_social_academica.model.*;
import red_social_academica.red_social_academica.repository.*;
import red_social_academica.red_social_academica.service.impl.CommentServiceImpl;
import red_social_academica.red_social_academica.validation.CommentValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock private CommentRepository commentRepository;
    @Mock private UserRepository userRepository;
    @Mock private PostRepository postRepository;
    @Mock private CommentValidator commentValidator;
    @Mock private INotificationService notificationService; // <-- FALTABA

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearComentario_valido_retornaDTO() {
        String username = "ana";
        CommentCreateDTO dto = CommentCreateDTO.builder()
                .postId(101L)
                .content("Muy interesante!")
                .build();

        User user = User.builder().id(1L).username(username).name("Ana").lastName("Gómez").activo(true).build();
        Post post = Post.builder().id(101L).title("Post").activo(true).user(user).build();

        Comment savedComment = Comment.builder()
                .id(999L)
                .content(dto.getContent())
                .createdAt(new Date())
                .author(user)
                .post(post)
                .activo(true)
                .build();

        when(userRepository.findByUsernameAndActivoTrue(username)).thenReturn(Optional.of(user));
        when(postRepository.findByIdAndActivoTrue(dto.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentDTO resultado = commentService.crearComentario(username, dto);

        assertThat(resultado.getId()).isEqualTo(999L);
        assertThat(resultado.getContent()).isEqualTo("Muy interesante!");
        assertThat(resultado.getAuthorId()).isEqualTo(user.getId());
        assertThat(resultado.getPostId()).isEqualTo(post.getId());

        verify(commentValidator).validarCreacion(dto);
        verify(notificationService, never()).crearNotificacion(any(), any(), any()); // como se comenta sobre su propio post
    }
}
