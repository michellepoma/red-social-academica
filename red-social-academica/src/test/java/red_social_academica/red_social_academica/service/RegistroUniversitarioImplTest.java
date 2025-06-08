package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.repository.RegistroUniversitarioRepository;
import red_social_academica.red_social_academica.service.impl.RegistroUniversitarioServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RegistroUniversitarioServiceImplTest {

    @Mock
    private RegistroUniversitarioRepository repository;

    @InjectMocks
    private RegistroUniversitarioServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ruExistente_devuelveTrue() {
        when(repository.existsByRu("1822345")).thenReturn(true);

        boolean resultado = service.esRuValido("1822345");

        assertThat(resultado).isTrue();
        verify(repository).existsByRu("1822345");
    }

    @Test
    void ruInexistente_devuelveFalse() {
        when(repository.existsByRu("0000000")).thenReturn(false);

        boolean resultado = service.esRuValido("0000000");

        assertThat(resultado).isFalse();
        verify(repository).existsByRu("0000000");
    }
}