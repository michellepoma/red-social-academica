package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.RegistroUniversitario;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RegistroUniversitarioRepositoryIntegrationTest {

    @Autowired
    private RegistroUniversitarioRepository repository;

    @Test
    void guardarYVerificarExistenciaPorRu() {
        RegistroUniversitario ru = repository.save(RegistroUniversitario.builder()
                .ru("1822301")
                .build());

        boolean existe = repository.existsByRu("1822301");
        assertThat(existe).isTrue();
    }

    @Test
    void ruNoExistente_retornaFalse() {
        boolean existe = repository.existsByRu("9999999");
        assertThat(existe).isFalse();
    }
}