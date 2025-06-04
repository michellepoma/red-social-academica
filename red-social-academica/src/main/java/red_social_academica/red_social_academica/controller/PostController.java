package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.post.*;
import red_social_academica.red_social_academica.service.IPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import static red_social_academica.red_social_academica.auth.security.AuthUtils.getCurrentUsername;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Publicaciones", description = "Operaciones públicas sobre publicaciones académicas")
public class PostController {

    private final IPostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Crear una nueva publicación (usuario autenticado)")
    @PostMapping
    public ResponseEntity<PostDTO> crearPost(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio crearPost: {}", inicio);

        String username = getCurrentUsername();
        PostDTO nueva = postService.crearPost(username, postCreateDTO);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin crearPost: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar una publicación propia")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> actualizarPost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio actualizarPost: {}", inicio);

        PostDTO actualizada = postService.actualizarPostPropio(postId, postUpdateDTO);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin actualizarPost: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar lógicamente una publicación propia")
    @PutMapping("/{postId}/baja")
    public ResponseEntity<PostDTO> eliminarPost(@PathVariable Long postId, @RequestParam String motivo) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio eliminarPost: {}", inicio);

        PostDTO eliminada = postService.eliminarPostPropio(postId, motivo);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin eliminarPost: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(eliminada);
    }

    @Operation(summary = "Obtener detalles de una publicación activa por ID")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> obtenerPostPorId(@PathVariable Long postId) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio obtenerPostPorId: {}", inicio);

        PostDTO dto = postService.obtenerPorId(postId);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin obtenerPostPorId: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Listar publicaciones activas de un usuario")
    @GetMapping("/usuario/{username}")
    public ResponseEntity<Page<PostDTO>> obtenerPostsDeUsuario(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio obtenerPostsDeUsuario: {}", inicio);

        Pageable pageable = PageRequest.of(page, size);
        Page<PostDTO> pageResult = postService.obtenerPostsDeUsuario(username, pageable);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin obtenerPostsDeUsuario: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(pageResult);
    }

    @Operation(summary = "Buscar publicaciones activas por texto en título o contenido")
    @GetMapping("/buscar")
    public ResponseEntity<Page<PostDTO>> buscarPorTexto(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio buscarPorTexto: {}", inicio);

        Pageable pageable = PageRequest.of(page, size);
        Page<PostDTO> resultado = postService.buscarPostsPorTexto(texto, pageable);

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin buscarPorTexto: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Obtener las 10 publicaciones activas más recientes")
    @GetMapping("/recientes")
    public ResponseEntity<List<PostDTO>> obtenerTop10Recientes() {
        long inicio = System.currentTimeMillis();
        logger.info("[POST] Inicio obtenerTop10Recientes: {}", inicio);

        List<PostDTO> lista = postService.obtenerTop10PublicacionesRecientes();

        long fin = System.currentTimeMillis();
        logger.info("[POST] Fin obtenerTop10Recientes: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(lista);
    }
}
