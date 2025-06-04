package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // === Publicaciones activas por usuario ===
    Page<Post> findByUserUsernameAndActivoTrue(String username, Pageable pageable);

    // === Búsqueda en publicaciones activas por título o texto ===
    @Query("""
        SELECT p FROM Post p
        WHERE p.activo = true AND (
            LOWER(p.title) LIKE LOWER(CONCAT('%', :text, '%')) 
            OR LOWER(p.text) LIKE LOWER(CONCAT('%', :text, '%'))
        )
        """)
    Page<Post> searchByTitleOrText(@Param("text") String text, Pageable pageable);

    // === Obtener top 10 publicaciones activas por fecha de publicación (desc) ===
    List<Post> findTop10ByActivoTrueOrderByDateDesc();

    // === Buscar por ID solo si está activo (útil para obtener detalles) ===
    Optional<Post> findByIdAndActivoTrue(Long id);

    //Admin: Ver post eliminados
    Page<Post> findByActivoFalse(Pageable pageable); // Opcional para panel de administración

}