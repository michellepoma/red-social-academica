package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.auth.model.Role;
import red_social_academica.red_social_academica.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // === BASICOS (con y sin activo) ===

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndActivoTrue(String username);

    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndActivoTrue(String email);

    boolean existsByUsername(String username);
    boolean existsByUsernameAndActivoTrue(String username);

    boolean existsByEmail(String email);
    boolean existsByEmailAndActivoTrue(String email);

    boolean existsByRu(String ru);
    boolean existsByRuAndActivoTrue(String ru);

    // === FILTROS POR ROL ===

    List<User> findAllByRoles_Nombre(Role.NombreRol nombre);
    List<User> findAllByRoles_NombreAndActivoTrue(Role.NombreRol nombre);

    Page<User> findAllByRoles_Nombre(Role.NombreRol nombre, Pageable pageable);
    Page<User> findAllByRoles_NombreAndActivoTrue(Role.NombreRol nombre, Pageable pageable);

    // === BÚSQUEDAS PERSONALIZADAS ===

    @Query("""
        SELECT u FROM User u
        WHERE u.activo = true AND 
              (LOWER(CONCAT(u.name, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchText, '%')) 
               OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%')))
        """)
    Page<User> searchByEmailAndName(@Param("searchText") String searchText,
                                          Pageable pageable);

    @Query("""
        SELECT u FROM User u 
        WHERE u.activo = true AND
              (:career IS NULL OR u.career = :career) AND
              (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) 
               OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
        """)
    Page<User> searchByCareerAndName(@Param("career") String career,
                                     @Param("search") String search,
                                     Pageable pageable);

    // === RELACIONES DE AMISTAD (filtrando activos) ===

    @Query("SELECT u FROM User u JOIN u.friends f WHERE f.username = :username AND u.activo = true")
    Page<User> getFriendsOf(@Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.friends f WHERE f.username = :username AND u.activo = true")
    List<User> getFriendsOf(@Param("username") String username);

    // === BLOQUEO PESIMISTA ===

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @NonNull
    Optional<User> findById(@NonNull Long id);
}
