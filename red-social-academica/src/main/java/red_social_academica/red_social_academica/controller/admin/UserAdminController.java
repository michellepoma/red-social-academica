package red_social_academica.red_social_academica.controller.admin;

import red_social_academica.red_social_academica.dto.user.*;
import red_social_academica.red_social_academica.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/usuarios")
@Tag(name = "Usuarios (Admin)", description = "Operaciones administrativas sobre usuarios")
public class UserAdminController {

    private final IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);

    @Autowired
    public UserAdminController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos los usuarios (activos e inactivos)")
    @GetMapping("/listar")
    public ResponseEntity<Page<UserDTO>> listarTodosUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio listarTodosUsuarios: {}", inicio);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> usuarios = userService.listarTodosUsuarios(pageable);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin listarTodosUsuarios: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Crear un nuevo usuario desde el panel de administrador")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente por el administrador")
    @PostMapping("/crear")
    public ResponseEntity<UserDTO> crearUsuarioComoAdmin(@Valid @RequestBody UserCreateAdminDTO userCreateAdminDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio crearUsuarioComoAdmin con rol: {}", userCreateAdminDTO.getRol());

        UserDTO creado = userService.crearUsuarioComoAdmin(userCreateAdminDTO);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin crearUsuarioComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Obtener perfil de cualquier usuario por username")
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> obtenerPorUsername(@PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio obtenerPorUsername: {}", inicio);

        UserDTO usuario = userService.obtenerPorUsername(username);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin obtenerPorUsername: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Actualizar datos de un usuario como administrador")
    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> actualizarUsuarioComoAdmin(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio actualizarUsuarioComoAdmin: {}", inicio);

        UserDTO actualizado = userService.actualizarUsuarioComoAdmin(username, userUpdateDTO);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin actualizarUsuarioComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Dar de baja a un usuario como administrador")
    @PutMapping("/{username}/baja")
    public ResponseEntity<UserDTO> eliminarUsuarioComoAdmin(@PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio eliminarUsuarioComoAdmin: {}", inicio);

        UserDTO eliminado = userService.eliminarUsuarioComoAdmin(username);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin eliminarUsuarioComoAdmin: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(eliminado);
    }

    @Operation(summary = "Listar usuarios por rol")
    @GetMapping("/rol/{role}")
    public ResponseEntity<Page<UserDTO>> obtenerPorRol(
            @PathVariable String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio obtenerPorRol: {}", inicio);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> usuarios = userService.obtenerPorRol(role, pageable);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin obtenerPorRol: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios por nombre o email")
    @GetMapping("/buscar")
    public ResponseEntity<Page<UserDTO>> buscarPorNombreYCorreo(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Inicio buscarPorNombreYCorreo con texto='{}'", texto);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> resultados = userService.buscarPorNombreYCorreo(texto, pageable);

        long fin = System.currentTimeMillis();
        logger.info("[ADMIN][USUARIO] Fin buscarPorNombreYCorreo: {} (Duración: {} ms)", fin, (fin - inicio));

        return ResponseEntity.ok(resultados);
    }
}
