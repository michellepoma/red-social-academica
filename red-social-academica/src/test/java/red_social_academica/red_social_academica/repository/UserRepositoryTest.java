package red_social_academica.red_social_academica.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import red_social_academica.red_social_academica.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debe guardar y recuperar un usuario por username")
    void shouldSaveAndFindUserByUsername() {
        // Arrange
        User user = User.builder()
                .email("prueba@universidad.edu")
                .username("usuario_test")
                .name("Carlos")
                .lastName("Pérez")
                .ru("1234567")
                .password("claveSegura123")
                .activo(true)
                .build();

        // set explícito después del builder
        user.setPasswordConfirm("claveSegura123");

        userRepository.save(user);

        // Act
        Optional<User> encontrado = userRepository.findByUsername("usuario_test");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("prueba@universidad.edu");
    }
}