package red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * Entidad JPA que representa una notificación dirigida a un usuario.
 * Las notificaciones pueden ser generadas por distintos eventos del sistema:
 * comentarios, invitaciones, publicaciones, etc. El mensaje es personalizable
 * y puede incluir un enlace para redirigir al recurso relacionado.
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(of = {"id", "message", "read", "createdAt"})
@EqualsAndHashCode(callSuper = true, of = "id")
public class Notification extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private boolean read;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "target_url")
    private String targetUrl;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipient_id", referencedColumnName = "id") // referencia columna PK
    private User recipient;

    @Column(nullable = false)
    private boolean activo;
}

