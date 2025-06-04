package red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import red_social_academica.red_social_academica.model.AuditableEntity;

import java.util.Date;

/**
 * Entidad JPA que representa un comentario realizado por un usuario
 * en una publicación. Incluye el contenido del comentario, la fecha de creación,
 * el autor y la publicación asociada.
 */
@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(of = {"id", "content", "createdAt"})
@EqualsAndHashCode(callSuper = true, of = "id")
public class Comment extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
