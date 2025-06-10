package red_social_academica.red_social_academica.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import red_social_academica.red_social_academica.dto.user.UserCreateDTO;
import red_social_academica.red_social_academica.dto.user.UserUpdateDTO;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IRegistroUniversitarioService;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    @Autowired
    private IRegistroUniversitarioService registroUniversitarioService;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validarCreacion(UserCreateDTO user) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (userRepository.existsByEmailAndActivoTrue(user.getEmail())) {
            errores.put("email", "Ya existe un usuario activo con este email");
        }

        if (userRepository.existsByUsernameAndActivoTrue(user.getUsername())) {
            errores.put("username", "Ya existe un usuario activo con este nombre de usuario");
        }

        if (!registroUniversitarioService.esRuValido(user.getRu())) {
            errores.put("ru", "El RU ingresado no pertenece a ningún estudiante autorizado");
        } else if (userRepository.existsByRuAndActivoTrue(user.getRu())) {
            errores.put("ru", "Ya existe un usuario activo con este Registro Universitario");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errores.put("name", "El nombre no puede estar vacío");
        }

        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            errores.put("lastName", "El apellido no puede estar vacío");
        }

        String dominio = user.getEmail().substring(user.getEmail().indexOf('@') + 1);
        List<String> dominiosBloqueados = List.of("spam.com", "bloqueado.edu", "correo.com");
        if (dominiosBloqueados.contains(dominio)) {
            errores.put("email", "El dominio del correo no está permitido");
        }

        if (!Objects.equals(user.getPassword(), user.getPasswordConfirm())) {
            throw new BusinessException("Las contraseñas no coinciden");
        }

        if (!errores.isEmpty()) {
            throw new BusinessException(errores.toString());
        }
    }

    public void validarEmailUnico(String email) {
        if (userRepository.existsByEmailAndActivoTrue(email)) {
            throw new BusinessException("Ya existe un usuario activo con este email");
        }
    }

    public void validarUsernameUnico(String username) {
        if (userRepository.existsByUsernameAndActivoTrue(username)) {
            throw new BusinessException("Ya existe un usuario activo con este nombre de usuario");
        }
    }

    public void validarRuUnico(String ru) {
        if (userRepository.existsByRuAndActivoTrue(ru)) {
            throw new BusinessException("Ya existe un usuario activo con este Registro Universitario");
        }
    }

    public void validarRuExiste(String ru) {
        if (!registroUniversitarioService.esRuValido(ru)) {
            throw new BusinessException("El RU ingresado no pertenece a ningún estudiante autorizado");
        }
    }

    public void validarDominioEmail(String email) {
        String dominio = email.substring(email.indexOf('@') + 1);
        List<String> dominiosBloqueados = Arrays.asList("spam.com", "bloqueado.edu", "correo.com");
        if (dominiosBloqueados.contains(dominio)) {
            throw new BusinessException("El dominio del correo no está permitido");
        }
    }

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre no puede estar vacío");
        }
    }

    public void validarApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new BusinessException("El apellido no puede estar vacío");
        }
    }

    public void validarActualizacion(UserUpdateDTO dto, String usernameActual) {
        Map<String, String> errores = new LinkedHashMap<>();

        // Validar si hay otro usuario con el mismo username
        User otroUsername = userRepository.findByUsernameAndActivoTrue(dto.getUsername()).orElse(null);
        if (otroUsername != null && !otroUsername.getUsername().equals(usernameActual)) {
            errores.put("username", "Ya existe otro usuario con este nombre de usuario");
        }

        // Validar si hay otro usuario con el mismo email
        User otroEmail = userRepository.findByEmailAndActivoTrue(dto.getEmail()).orElse(null);
        if (otroEmail != null && !otroEmail.getUsername().equals(usernameActual)) {
            errores.put("email", "Ya existe otro usuario con este correo");
        }

        // Validar dominio de email
        try {
            validarDominioEmail(dto.getEmail());
        } catch (BusinessException e) {
            errores.put("email", e.getMessage());
        }

        // Validar coincidencia de contraseñas si se proporciona una
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
                errores.put("passwordConfirm", "Las contraseñas no coinciden");
            }
        }

        // Si hay errores, lanzar excepción
        if (!errores.isEmpty()) {
            throw new BusinessException(errores.toString());
        }
    }

}
