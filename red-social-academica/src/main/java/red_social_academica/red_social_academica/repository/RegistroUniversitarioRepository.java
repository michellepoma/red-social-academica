package red_social_academica.red_social_academica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import red_social_academica.red_social_academica.model.RegistroUniversitario;

public interface RegistroUniversitarioRepository extends JpaRepository<RegistroUniversitario, Long> {
    boolean existsByRu(String ru);
}
