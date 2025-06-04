package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // === Comentarios activos por post (ordenados por fecha de creación ascendente) ===
    List<Comment> findByPostIdAndActivoTrueOrderByCreatedAtAsc(Long postId);

    // === Comentarios activos por autor ===
    List<Comment> findByAuthorUsernameAndActivoTrue(String username);

    // === Comentarios activos por autor paginados ===
    Page<Comment> findByAuthorUsernameAndActivoTrue(String username, Pageable pageable);

    // === Comentario activo por ID ===
    Optional<Comment> findByIdAndActivoTrue(Long id);
}