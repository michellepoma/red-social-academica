package red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.*;

import io.swagger.v3.oas.annotations.media.Schema;
import red_social_academica.red_social_academica.auth.model.Role;

@Entity
@Table(name = "app_user") // Renombrada para evitar conflicto con palabra reservada
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = "username")
@ToString(of = {"id", "username", "name", "lastName"})
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Correo electrónico del usuario", example = "ana.gomez@universidad.edu")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Nombre del usuario", example = "Ana")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "Apellido del usuario", example = "Gómez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Column(name = "user_name", unique = true, nullable = false)
    @Schema(description = "Nombre de usuario único", example = "ana_gomez")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    private String username;
    
    @Column(unique = true, nullable = false)
    @Schema(description = "Registro universitario unico", example = "1822345")
    @NotBlank(message = "El registro universitario es obligatorio")
    @Size(min = 7, message = "El registro universitario debe tener minimo 7 numeros")
    private String ru;

    @Column(nullable = false)
    @Schema(description = "Contraseña para el acceso", example = "segura123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Transient
    @Column(name = "password_confirm")
    @Schema(description = "Confirmación de contraseña", example = "segura123")
    @NotBlank(message = "Debes confirmar tu contraseña")
    private String passwordConfirm;

    @Column(name = "profile_picture_url")
    @Schema(description = "URL de la foto de perfil del usuario", example = "https://cdn.misitio.com/perfiles/ana.jpg")
    @Size(max = 255, message = "La URL no debe superar los 255 caracteres")
    private String profilePictureUrl;

    @Column(length = 300)
    @Schema(description = "Biografía o descripción corta del usuario", example = "Estudiante de ingeniería apasionada por la IA")
    @Size(max = 300, message = "La biografía no debe superar los 300 caracteres")
    private String bio;

    private String career;

    private LocalDate birthdate;

    @Builder.Default
    private boolean activo = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Relaciones
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "friend_id"),
            inverseJoinColumns = @JoinColumn(name = "aux_friend_id"))
    private Set<User> friends = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "friends")
    private Set<User> auxFriends = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> receivedInvitations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> sendedInvitations = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public String getFullName() {
        return this.name + " " + this.lastName;
    }

    public void borrarAmigos() {
        User[] amigos = friends.toArray(new User[0]);
        for (User amigo : amigos) {
            this.friends.remove(amigo);
            this.auxFriends.remove(amigo);
            amigo.getFriends().remove(this);
            amigo.getAuxFriends().remove(this);
        }
    }

    public boolean canInvite(String email) {
        if (this.email.equals(email)) return false;

        return friends.stream().noneMatch(f -> f.getEmail().equals(email)) &&
               auxFriends.stream().noneMatch(f -> f.getEmail().equals(email)) &&
               sendedInvitations.stream().noneMatch(i -> i.esDelUsuario(email)) &&
               receivedInvitations.stream().noneMatch(i -> i.esDelUsuario(email));
    }
}
