package red_social_academica.red_social_academica.auth.repository;

import red_social_academica.red_social_academica.auth.model.Role;
//import red_social_academica.red_social_academica.auth.model.Role.NombreRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * Repositorio para gestionar roles del sistema.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(Role.NombreRol nombre);
}
