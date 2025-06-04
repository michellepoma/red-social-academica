package red_social_academica.red_social_academica.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import red_social_academica.red_social_academica.repository.RegistroUniversitarioRepository;
import red_social_academica.red_social_academica.service.IRegistroUniversitarioService;

/**
 * Implementación del servicio para la gestión de registros universitarios.
 * Permite validar si un RU ingresado está autorizado para registrarse en el sistema.
 */
@Service
@RequiredArgsConstructor
public class RegistroUniversitarioServiceImpl implements IRegistroUniversitarioService {

    private final RegistroUniversitarioRepository registroUniversitarioRepository;

    /**
     * Valida si el RU proporcionado pertenece a un estudiante registrado en el sistema.
     *
     * @param ru número de matrícula ingresado
     * @return true si el RU existe, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "ruValidoCache", key = "#ru")
    public boolean esRuValido(String ru) {
        return registroUniversitarioRepository.existsByRu(ru);
    }
}
