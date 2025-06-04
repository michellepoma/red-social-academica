package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.user.*;
import red_social_academica.red_social_academica.service.IUserService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones para usuarios autenticados")
public class UserController {

    private final IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Actualizar perfil del usuario autenticado")
    @PutMapping("/me")
    public ResponseEntity<UserDTO> actualizarPerfil(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        long inicio = System.currentTimeMillis();
        logger.info("[USUARIO] Inicio actualizarPerfil: {}", inicio);

        UserDTO actualizado = userService.actualizarPerfil(userUpdateDTO);

        long fin = System.currentTimeMillis();
        logger.info("[USUARIO] Fin actualizarPerfil: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Obtener mi perfil")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> obtenerMiPerfil() {
        long inicio = System.currentTimeMillis();
        logger.info("[USUARIO] Inicio obtenerMiPerfil: {}", inicio);

        String username = getCurrentUsername();
        UserDTO usuario = userService.obtenerPorUsername(username);

        long fin = System.currentTimeMillis();
        logger.info("[USUARIO] Fin obtenerMiPerfil: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Eliminar lógicamente mi cuenta")
    @PutMapping("/me/baja")
    public ResponseEntity<UserDTO> eliminarUsuario() {
        long inicio = System.currentTimeMillis();
        logger.info("[USUARIO] Inicio eliminarUsuario");

        UserDTO eliminado = userService.eliminarUsuario();

        long fin = System.currentTimeMillis();
        logger.info("[USUARIO] Fin eliminarUsuario: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(eliminado);
    }

    @Operation(summary = "Ver perfil público de otro usuario")
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> obtenerPorUsername(@PathVariable String username) {
        long inicio = System.currentTimeMillis();
        logger.info("[USUARIO] Inicio obtenerPorUsername: {} para {}", inicio, username);

        UserDTO usuario = userService.obtenerPorUsername(username);

        long fin = System.currentTimeMillis();
        logger.info("[USUARIO] Fin obtenerPorUsername: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtener lista de amigos del usuario")
    @GetMapping("/me/amigos")
    public ResponseEntity<List<UserDTO>> obtenerAmigos() {
        long inicio = System.currentTimeMillis();
        logger.info("[USUARIO] Inicio obtenerAmigos: {}", inicio);

        List<UserDTO> amigos = userService.obtenerAmigos(getCurrentUsername());

        long fin = System.currentTimeMillis();
        logger.info("[USUARIO] Fin obtenerAmigos: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(amigos);
    }

    @Operation(summary = "Obtener lista paginada de amigos")
    @GetMapping("/me/amigos/page")
    public ResponseEntity<Page<UserDTO>> obtenerAmigosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        long inicio = System.currentTimeMillis();
        logger.info("[USUARIO] Inicio obtenerAmigosPaginados: {}", inicio);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> amigos = userService.obtenerAmigos(getCurrentUsername(), pageable);

        long fin = System.currentTimeMillis();
        logger.info("[USUARIO] Fin obtenerAmigosPaginados: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(amigos);
    }
}
