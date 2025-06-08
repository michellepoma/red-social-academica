package red_social_academica.red_social_academica.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @Test
    void construirComment_conDatosBasicos_funciona() {
        User autor = User.builder().id(1L).username("juan").name("Juan").lastName("Pérez").build();
        Post post = Post.builder().id(100L).title("Post de prueba").build();
        Date ahora = new Date();

        Comment c = Comment.builder()
                .id(55L)
                .content("Muy bueno tu post")
                .createdAt(ahora)
                .author(autor)
                .post(post)
                .activo(true)
                .build();

        assertThat(c.getId()).isEqualTo(55L);
        assertThat(c.getContent()).isEqualTo("Muy bueno tu post");
        assertThat(c.getCreatedAt()).isEqualTo(ahora);
        assertThat(c.getAuthor()).isEqualTo(autor);
        assertThat(c.getPost()).isEqualTo(post);
        assertThat(c.isActivo()).isTrue();
    }
}