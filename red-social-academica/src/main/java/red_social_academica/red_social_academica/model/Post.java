package red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa una publicación realizada por un usuario.
 * Soporta múltiples formatos combinables: texto, imagen, recurso y evento.
 * Todos los campos adicionales son opcionales para lograr flexibilidad.
 */
@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(of = {"id", "title", "date"})
@EqualsAndHashCode(callSuper = true, of = "id")
public class Post extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // URL de imagen (opcional)
    @Column(name = "image_url")
    private String imageUrl;

    // URL de recurso (PDF, documento, etc.)
    @Column(name = "resource_url")
    private String resourceUrl;

    // Fecha de evento (opcional)
    @Column(name = "event_date")
    private LocalDate eventDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    // Comentarios relacionados a esta publicación
    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
