package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    // === Invitaciones activas o no enviadas por un usuario (admin)===
    List<Invitation> findAllByActivoTrue();

    // === Invitaciones activas enviadas por un usuario ===
    List<Invitation> findBySenderUsernameAndActivoTrue(String username);

    // === Invitaciones activas recibidas por un usuario ===
    List<Invitation> findByReceiverUsernameAndActivoTrue(String username);

    // === Buscar invitacion activa entre dos usuarios ===
    Optional<Invitation> findBySenderUsernameAndReceiverUsernameAndActivoTrue(String sender, String receiver);

    // === Verifica si ya existe una invitacion activa entre dos usuarios ===
    boolean existsBySenderUsernameAndReceiverUsernameAndActivoTrue(String sender, String receiver);

}
