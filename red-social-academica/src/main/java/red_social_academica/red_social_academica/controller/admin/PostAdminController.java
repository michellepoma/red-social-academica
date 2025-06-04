package red_social_academica.red_social_academica.controller.admin;

import red_social_academica.red_social_academica.dto.post.*;
import red_social_academica.red_social_academica.service.IPostService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/posts")
@Tag(name = "Publicaciones (Admin)", description = "Gestión de publicaciones como administrador")
public class PostAdminController {

    private static final Logger logger = LoggerFactory.getLogger(PostAdminController.class);

    private final IPostService postService;

    @Autowired
    public PostAdminController(IPostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Actualizar cualquier publicación como administrador")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> actualizarPostComoAdmin(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateDTO dto) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio actualizarPostComoAdmin: {}", inicio);

        PostDTO actualizado = postService.actualizarPostComoAdmin(postId, dto);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin actualizarPostComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cualquier publicación como administrador")
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostDTO> eliminarPostComoAdmin(
            @PathVariable Long postId,
            @RequestParam String motivo) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio eliminarPostComoAdmin: {}", inicio);

        PostDTO eliminado = postService.eliminarPostComoAdmin(postId, motivo);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin eliminarPostComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(eliminado);
    }

    @Operation(summary = "Obtener detalles de una publicación activa por ID")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> obtenerPostPorId(@PathVariable Long postId) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio obtenerPostPorId: {}", inicio);

        PostDTO post = postService.obtenerPorId(postId);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin obtenerPostPorId: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(post);
    }
}
