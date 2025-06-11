package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.auth.model.Role;
import red_social_academica.red_social_academica.auth.model.Role.NombreRol;
import red_social_academica.red_social_academica.auth.repository.RoleRepository;
import red_social_academica.red_social_academica.auth.security.AuthUtils;
import red_social_academica.red_social_academica.dto.user.*;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IUserService;
import red_social_academica.red_social_academica.validation.UserValidator;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

import static red_social_academica.red_social_academica.auth.security.AuthUtils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // === PUBLIC ===

    @Override
    @Transactional
    @CacheEvict(value = {
            "userByUsernameCache",
            "usersByRoleCache",
            "usersBySearchCache",
            "friendsCache"
    }, key = "T(red_social_academica.red_social_academica.auth.security.AuthUtils).getCurrentUsername()")
    public UserDTO actualizarPerfil(UserUpdateDTO dto) {
        String usernameActual = getCurrentUsername();

        User user = userRepository.findByUsernameAndActivoTrue(usernameActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        userValidator.validarActualizacion(dto, usernameActual);

        // Si el usuario proporciona una nueva contraseña, se codifica y se actualiza
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        updateEntityFromUpdateDTO(dto, user);

        user.setFechaModificacion(LocalDate.now());
        user.setUsuarioModificacion(usernameActual);

        return convertToDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    @CacheEvict(value = { "userByUsernameCache", "usersByRoleCache", "usersBySearchCache",
            "friendsCache" }, key = "T(red_social_academica.red_social_academica.auth.security.AuthUtils).getCurrentUsername()")
    public UserDTO eliminarUsuario() {
        String usernameActual = getCurrentUsername();
        User user = userRepository.findByUsernameAndActivoTrue(usernameActual)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o ya eliminado"));

        marcarUsuarioComoBaja(user, "solicitud del usuario", usernameActual);

        return convertToDTO(userRepository.save(user));
    }

    // === ADMIN ===

    @Override
    @Transactional
    @CacheEvict(value = { "userByUsernameCache", "usersByRoleCache", "usersBySearchCache", "friendsCache",
            "allUsersCache" }, allEntries = true)
    public UserDTO crearUsuarioComoAdmin(UserCreateAdminDTO dto) {
        userValidator.validarCreacion(dto);
        validarContrasenasIguales(dto.getPassword(), dto.getPasswordConfirm());

        User user = mapFromCreateDTO(dto);
        user.setActivo(true);
        user.setUsuarioAlta(getCurrentUsername());

        Role rol = obtenerRolDesdeString(dto.getRol());
        user.setRoles(Set.of(rol));

        User guardado = userRepository.save(user);
        return convertToDTO(guardado);
    }

    @Override
    @Transactional
    @CacheEvict(value = { "userByUsernameCache", "usersByRoleCache", "usersBySearchCache", "friendsCache",
            "allUsersCache" }, key = "#username", allEntries = true)

    public UserDTO actualizarUsuarioComoAdmin(String username, UserUpdateDTO dto) {
        if (!isAdmin()) {
            throw new SecurityException("Solo los administradores pueden usar esta función");
        }

        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        updateEntityFromUpdateDTO(dto, user);
        user.setFechaModificacion(LocalDate.now());
        user.setUsuarioModificacion(getCurrentUsername());

        return convertToDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    @CacheEvict(value = { "userByUsernameCache", "usersByRoleCache", "usersBySearchCache", "friendsCache",
            "allUsersCache" }, key = "#username", allEntries = true)
    public UserDTO eliminarUsuarioComoAdmin(String username) {
        if (!isAdmin()) {
            throw new SecurityException("Solo los administradores pueden usar esta función");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o ya eliminado"));

        marcarUsuarioComoBaja(user, "eliminado por administrador", getCurrentUsername());

        return convertToDTO(userRepository.save(user));
    }

    @Override
    @Cacheable(value = "allUsersCache")
    public Page<UserDTO> listarTodosUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    // === LECTURA ===

    @Override
    @Cacheable(value = "userByUsernameCache", key = "#username")
    public UserDTO obtenerPorUsername(String username) {
        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));
        return convertToDTO(user);
    }

    @Override
    @Cacheable(value = "usersBySearchCache", key = "#texto + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<UserDTO> buscarPorNombreYCorreo(String texto, Pageable pageable) {
        texto = texto.trim().toLowerCase();
        Page<User> paginaUsuarios = userRepository.searchAllFields(texto, pageable);

        // Filtro adicional si el texto es "activo" o "inactivo"
        if (texto.equalsIgnoreCase("activo")) {
            paginaUsuarios = new PageImpl<>(
                    paginaUsuarios.stream().filter(User::isActivo).toList(),
                    pageable,
                    paginaUsuarios.getTotalElements());
        } else if (texto.equalsIgnoreCase("inactivo")) {
            paginaUsuarios = new PageImpl<>(
                    paginaUsuarios.stream().filter(u -> !u.isActivo()).toList(),
                    pageable,
                    paginaUsuarios.getTotalElements());
        }

        return paginaUsuarios.map(this::convertToDTO);
    }

    @Override
    @Cacheable(value = "friendsCache", key = "#username")
    public List<UserDTO> obtenerAmigos(String username) {
        return userRepository.getFriendsOf(username).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "friendsCache", key = "#username + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<UserDTO> obtenerAmigos(String username, Pageable pageable) {
        return userRepository.getFriendsOf(username, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public boolean existePorUsername(String username) {
        return userRepository.findByUsernameAndActivoTrue(username).isPresent();
    }

    @Override
    public boolean existePorEmail(String email) {
        return userRepository.findByEmailAndActivoTrue(email).isPresent();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "friendsCache", allEntries = true)
    })

    public void eliminarAmistad(String username1, String username2) {
        User user1 = userRepository.findByUsernameAndActivoTrue(username1)
                .orElseThrow(() -> new RuntimeException("Usuario 1 no encontrado o inactivo"));

        User user2 = userRepository.findByUsernameAndActivoTrue(username2)
                .orElseThrow(() -> new RuntimeException("Usuario 2 no encontrado o inactivo"));

        user1.getFriends().remove(user2);
        user1.getAuxFriends().remove(user2);
        user2.getFriends().remove(user1);
        user2.getAuxFriends().remove(user1);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    // === UTILITARIOS PRIVADOS ===

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .ru(user.getRu())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .career(user.getCareer())
                .profilePictureUrl(user.getProfilePictureUrl())
                .birthdate(user.getBirthdate())
                .fechaAlta(user.getFechaAlta())
                .fechaModificacion(user.getFechaModificacion())
                .fechaBaja(user.getFechaBaja())
                .motivoBaja(user.getMotivoBaja())
                .activo(user.isActivo())
                .roles(user.getRoles().stream()
                        .map(role -> role.getNombre().name())
                        .collect(Collectors.toList()))
                .build();
    }

    private User mapFromCreateDTO(UserCreateDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .ru(dto.getRu())
                .email(dto.getEmail())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .career(dto.getCareer())
                .bio(dto.getBio())
                .birthdate(dto.getBirthdate())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }

    private void updateEntityFromUpdateDTO(UserUpdateDTO dto, User user) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setBio(dto.getBio());
        user.setCareer(dto.getCareer());
        user.setBirthdate(dto.getBirthdate());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
    }

    private void validarContrasenasIguales(String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw new BusinessException("Las contraseñas no coinciden");
        }
    }

    private Role obtenerRolDesdeString(String rolStr) {
        try {
            NombreRol nombreRol = NombreRol.valueOf(rolStr);
            return roleRepository.findByNombre(nombreRol)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolStr));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol inválido: " + rolStr);
        }
    }

    private void marcarUsuarioComoBaja(User user, String motivo, String quien) {
        user.setActivo(false);
        user.setFechaBaja(LocalDate.now());
        user.setMotivoBaja(motivo);
        user.setUsuarioBaja(quien);
    }

    private String getCurrentUsername() {
        return AuthUtils.getCurrentUsername();
    }

}
