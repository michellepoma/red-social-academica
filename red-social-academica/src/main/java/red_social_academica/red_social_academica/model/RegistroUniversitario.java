package red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entidad JPA que representa un registro universitario válido.
 * Utilizado para validar que un usuario pertenece a la universidad
 * al momento de registrarse en la red social académica.
 */
@Entity
@Table(name = "registro_universitario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(of = {"id", "ru"})
@EqualsAndHashCode(callSuper = false, of = "ru")
public class RegistroUniversitario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Registro Universitario (número de matrícula).
     * Debe ser único y no nulo.
     */
    @Column(nullable = false, unique = true, length = 20)
    private String ru;
}

