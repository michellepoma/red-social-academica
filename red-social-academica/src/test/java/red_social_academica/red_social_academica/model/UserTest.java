package red_social_academica.red_social_academica.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void userConEmailNuloDebeSerInvalido() {
        User user = User.builder()
                .username("juan123")
                .name("Juan")
                .lastName("Perez")
                .ru("123456")
                .password("123")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void userNoPuedeInvitarseASiMismo() {
        User user = User.builder().email("yo@ejemplo.com").build();
        assertThat(user.canInvite("yo@ejemplo.com")).isFalse();
    }

    @Test
    void getFullNameDevuelveNombreCompleto() {
        User user = User.builder().name("Ana").lastName("López").build();
        assertThat(user.getFullName()).isEqualTo("Ana López");
    }
}
