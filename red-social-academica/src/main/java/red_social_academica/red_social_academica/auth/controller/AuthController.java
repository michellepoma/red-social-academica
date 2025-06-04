package red_social_academica.red_social_academica.auth.controller;

import red_social_academica.red_social_academica.auth.dto.AuthDTO.*;
import red_social_academica.red_social_academica.auth.model.Role;
import red_social_academica.red_social_academica.auth.model.Role.NombreRol;
import red_social_academica.red_social_academica.auth.repository.RoleRepository;
import red_social_academica.red_social_academica.auth.security.JwtUtils;
import red_social_academica.red_social_academica.dto.user.UserCreateDTO;
import red_social_academica.red_social_academica.dto.user.UserDTO;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.validation.UserValidator;
import red_social_academica.red_social_academica.validation.exception.BusinessException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las operaciones de autenticación y registro de
 * usuarios.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserValidator userValidator;

    /**
     * Autenticación de usuario.
     */
    @Operation(summary = "Iniciar sesion")
    @ApiResponse(responseCode = "200", description = "Sesion iniciada con exito")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getNombre().name()) // Ej: "ROLE_ADMIN"
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        // Validaciones personalizadas
        userValidator.validarCreacion(userCreateDTO);

        if (!userCreateDTO.getPassword().equals(userCreateDTO.getPasswordConfirm())) {
            throw new BusinessException("Las contraseñas no coinciden");
        }

        // Crear entidad User
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setUsuarioAlta(user.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        user.setName(userCreateDTO.getName());
        user.setLastName(userCreateDTO.getLastName());
        user.setProfilePictureUrl(userCreateDTO.getProfilePictureUrl());
        user.setBio(userCreateDTO.getBio());
        user.setCareer(userCreateDTO.getCareer());
        user.setBirthdate(userCreateDTO.getBirthdate());
        user.setRu(userCreateDTO.getRu());
        user.setActivo(true);
        

        // Roles
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByNombre(NombreRol.ROLE_PUBLIC)
                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
        roles.add(defaultRole);
        user.setRoles(roles);

        // Guardar en base de datos
        User savedUser = userRepository.save(user);

        // Construir respuesta DTO
        UserDTO userDTO = UserDTO.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .name(savedUser.getName())
                .lastName(savedUser.getLastName())
                .profilePictureUrl(savedUser.getProfilePictureUrl())
                .bio(savedUser.getBio())
                .career(savedUser.getCareer())
                .birthdate(savedUser.getBirthdate())
                .ru(savedUser.getRu())
                .usuarioAlta(savedUser.getUsername())
                .fechaAlta(savedUser.getFechaAlta())
                .fechaModificacion(savedUser.getFechaModificacion())
                .fechaBaja(savedUser.getFechaBaja())
                .motivoBaja(savedUser.getMotivoBaja())
                .activo(savedUser.isActivo())
                .roles(savedUser.getRoles().stream()
                        .map(role -> role.getNombre().name())
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.status(201).body(userDTO);
    }

    /**
     * Devuelve información de la sesión actual.
     */
    @Operation(summary = "Devuelve información de la sesión actual")
    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = (String) auth.getPrincipal(); // ✅ CAMBIO HECHO AQUÍ
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

            Set<String> roles = user.getRoles().stream()
                    .map(r -> r.getNombre().name())
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(new JwtResponse(
                    null, // no nuevo token
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    roles));
        }
        return ResponseEntity.ok(new MessageResponse("No hay sesión activa"));
    }

    /**
     * Cierra la sesión del usuario actual (frontend local).
     */
    @PostMapping("/logout")
    @Operation(summary = "Cierra la sesión del usuario actual (frontend local).")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
    }
}
