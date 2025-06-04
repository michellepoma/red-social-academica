package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.comment.*;
import red_social_academica.red_social_academica.service.ICommentService;
import static red_social_academica.red_social_academica.auth.security.AuthUtils.getCurrentUsername;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comentarios", description = "Operaciones públicas sobre comentarios")
public class CommentController {

    private final ICommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    // === Crear ===
    @Operation(summary = "Crear un nuevo comentario")
    @PostMapping
    public ResponseEntity<CommentDTO> crearComentario(@Valid @RequestBody CommentCreateDTO dto) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMENTARIO] Inicio crearComentario: {}", inicio);

        String username = getCurrentUsername();
        CommentDTO nuevo = commentService.crearComentario(username, dto);

        long fin = System.currentTimeMillis();
        logger.info("[COMENTARIO] Fin crearComentario: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // === Leer ===
    @Operation(summary = "Obtener comentarios activos de un post")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> obtenerComentariosDePost(@PathVariable Long postId) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMENTARIO] Inicio obtenerComentariosDePost: {}", inicio);

        List<CommentDTO> comentarios = commentService.obtenerComentariosDePost(postId);

        long fin = System.currentTimeMillis();
        logger.info("[COMENTARIO] Fin obtenerComentariosDePost: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(comentarios);
    }

    @Operation(summary = "Obtener comentarios activos de un usuario (sin paginación)")
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<CommentDTO>> obtenerComentariosDeUsuario(@PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMENTARIO] Inicio obtenerComentariosDeUsuario: {}", inicio);

        List<CommentDTO> comentarios = commentService.obtenerComentariosDeUsuario(username);

        long fin = System.currentTimeMillis();
        logger.info("[COMENTARIO] Fin obtenerComentariosDeUsuario: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(comentarios);
    }

    @Operation(summary = "Obtener comentarios activos de un usuario (paginado)")
    @GetMapping("/usuario/{username}/paginado")
    public ResponseEntity<Page<CommentDTO>> obtenerComentariosDeUsuarioPaginado(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMENTARIO] Inicio obtenerComentariosDeUsuarioPaginado: {}", inicio);

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDTO> comentarios = commentService.obtenerComentariosDeUsuario(username, pageable);

        long fin = System.currentTimeMillis();
        logger.info("[COMENTARIO] Fin obtenerComentariosDeUsuarioPaginado: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(comentarios);
    }

    // === Actualizar ===
    @Operation(summary = "Actualizar tu propio comentario")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> actualizarComentario(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDTO dto) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMENTARIO] Inicio actualizarComentario: {}", inicio);

        CommentDTO actualizado = commentService.actualizarComentarioPropio(commentId, dto);

        long fin = System.currentTimeMillis();
        logger.info("[COMENTARIO] Fin actualizarComentario: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(actualizado);
    }

    // === Eliminar ===
    @Operation(summary = "Eliminar lógicamente tu comentario")
    @PutMapping("/{commentId}/baja")
    public ResponseEntity<CommentDTO> eliminarComentario(
            @PathVariable Long commentId,
            @RequestParam String motivo) {
        long inicio = System.currentTimeMillis();
        logger.info("[COMENTARIO] Inicio eliminarComentario: {}", inicio);

        CommentDTO eliminado = commentService.eliminarComentarioPropio(commentId, motivo);

        long fin = System.currentTimeMillis();
        logger.info("[COMENTARIO] Fin eliminarComentario: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(eliminado);
    }

}
