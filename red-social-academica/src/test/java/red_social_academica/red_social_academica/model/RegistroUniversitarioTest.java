package red_social_academica.red_social_academica.model;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistroUniversitarioTest {

    @Test
    void crearRegistroUniversitario_conValoresCorrectos() {
        RegistroUniversitario ru = RegistroUniversitario.builder()
                .ru("1822345")
                .build();

        assertThat(ru.getRu()).isEqualTo("1822345");
    }

    @Test
    void equalsYHashCode_funcionanCorrectamente() {
        RegistroUniversitario ru1 = RegistroUniversitario.builder().ru("1822345").build();
        RegistroUniversitario ru2 = RegistroUniversitario.builder().ru("1822345").build();

        assertThat(ru1).isEqualTo(ru2);
        assertThat(ru1.hashCode()).isEqualTo(ru2.hashCode());
    }

    @Test
    void toStringIncluyeRu() {
        RegistroUniversitario ru = RegistroUniversitario.builder().ru("1822345").build();
        assertThat(ru.toString()).contains("1822345");
    }
}