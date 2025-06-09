package red_social_academica.red_social_academica.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import red_social_academica.red_social_academica.model.Post;
import red_social_academica.red_social_academica.model.User;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PostRepositoryIntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debe guardar y recuperar un post por usuario")
    void testGuardarYRecuperarPost() {
        User user = User.builder()
                .username("juanito")
                .email("juanito@example.com")
                .name("Juan")
                .lastName("Pérez")
                .password("segura123") // ✅ Contraseña válida
                .ru("1234567") // ✅ RU válido (7 números)
                .activo(true)
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("Título del Post")
                .text("Texto del post de prueba")
                .date(new Date())
                .activo(true)
                .user(user)
                .build();
        postRepository.save(post);

        List<Post> encontrados = postRepository.findTop10ByActivoTrueOrderByDateDesc();
        assertThat(encontrados).isNotEmpty();
        assertThat(encontrados.get(0).getTitle()).isEqualTo("Título del Post");
    }
}