package red_social_academica.red_social_academica.auth.config;

import red_social_academica.red_social_academica.auth.model.Role;
import red_social_academica.red_social_academica.auth.model.Role.NombreRol;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.auth.repository.RoleRepository;
import red_social_academica.red_social_academica.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//import java.util.HashSet;
import java.util.Set;

/**
 * Inicializa roles y un usuario administrador por defecto al arrancar la
 * aplicación.
 */
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        inicializarRoles();
        crearAdminPorDefecto();
    }

    private void inicializarRoles() {
        if (rolRepository.count() == 0) {
            Role admin = Role.builder().nombre(NombreRol.ROLE_ADMIN).build();
            Role publicRole = Role.builder().nombre(NombreRol.ROLE_PUBLIC).build();
            rolRepository.save(admin);
            rolRepository.save(publicRole);
            System.out.println("✔ Roles creados: ROLE_ADMIN, ROLE_PUBLIC");
        }
    }

    private void crearAdminPorDefecto() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@redsocial.com")
                    .name("Administrador")
                    .lastName("Sistema")
                    .password(passwordEncoder.encode("admin123"))
                    .passwordConfirm("admin123") // ← campo requerido
                    .ru("100200300") // ← campo requerido (puede ser cualquier string válido)
                    .roles(Set.of(
                            rolRepository.findByNombre(NombreRol.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"))))
                    .build();

            userRepository.save(admin);
            System.out.println("✔ Usuario admin creado: admin / admin123");
        }
    }
}