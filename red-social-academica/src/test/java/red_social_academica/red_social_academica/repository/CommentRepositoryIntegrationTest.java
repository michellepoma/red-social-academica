package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryIntegrationTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .username("carlos")
                .name("Carlos")
                .lastName("Lopez")
                .email("carlos@example.com")
                .ru("1234567") // ahora sí cumple con el @NotBlank y @Size
                .password("segura123") // cumple con el @NotBlank y @Size
                .activo(true)
                .build());

        post = postRepository.save(Post.builder()
                .title("Nuevo post")
                .text("Contenido del post")
                .user(user)
                .activo(true)
                .build());
    }

    @Test
    void guardarYBuscarComentarioPorPostIdYActivoTrue_funciona() {
        Comment comment = commentRepository.save(Comment.builder()
                .content("Comentario de prueba")
                .createdAt(new Date())
                .author(user)
                .post(post)
                .activo(true)
                .build());

        List<Comment> comentarios = commentRepository.findByPostIdAndActivoTrueOrderByCreatedAtAsc(post.getId());

        assertThat(comentarios).isNotEmpty();
        assertThat(comentarios.get(0).getContent()).isEqualTo("Comentario de prueba");
    }
}