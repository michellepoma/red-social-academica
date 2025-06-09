package red_social_academica.red_social_academica.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void postConTituloNuloDebeSerInvalido() {
        Post post = Post.builder()
                .text("Contenido del post")
                .date(new Date())
                .build();

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    void postValidoDebePasarValidacion() {
        Post post = Post.builder()
                .title("Título válido")
                .text("Contenido del post")
                .date(new Date())
                .build();

        Set<ConstraintViolation<Post>> violations = validator.validate(post);
        assertThat(violations).isEmpty();
    }
}