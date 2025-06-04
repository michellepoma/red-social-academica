package red_social_academica.red_social_academica.controller.admin;

import red_social_academica.red_social_academica.dto.comment.CommentDTO;
import red_social_academica.red_social_academica.dto.comment.CommentUpdateDTO;
import red_social_academica.red_social_academica.service.ICommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/comments")
@Tag(name = "Admin - Comentarios", description = "Gestión de comentarios como administrador")
public class CommentAdminController {

    private static final Logger logger = LoggerFactory.getLogger(CommentAdminController.class);

    private final ICommentService commentService;

    @Autowired
    public CommentAdminController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Actualizar cualquier comentario como admin")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> actualizarComentarioComoAdmin(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDTO dto) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMMENT] Inicio actualizarComentarioComoAdmin: {}", inicio);

        CommentDTO actualizado = commentService.actualizarComentarioComoAdmin(commentId, dto);

        long fin = System.currentTimeMillis();
        logger.info("[COMMENT] Fin actualizarComentarioComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cualquier comentario como admin")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDTO> eliminarComentarioComoAdmin(
            @PathVariable Long commentId,
            @RequestParam String motivo) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMMENT] Inicio eliminarComentarioComoAdmin: {}", inicio);

        CommentDTO eliminado = commentService.eliminarComentarioComoAdmin(commentId, motivo);

        long fin = System.currentTimeMillis();
        logger.info("[COMMENT] Fin eliminarComentarioComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(eliminado);
    }
}
