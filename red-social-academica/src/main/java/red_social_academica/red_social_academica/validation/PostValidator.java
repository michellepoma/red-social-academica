package red_social_academica.red_social_academica.validation;

import org.springframework.stereotype.Component;
import red_social_academica.red_social_academica.dto.post.PostCreateDTO;
import red_social_academica.red_social_academica.dto.post.PostUpdateDTO;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostValidator {

    public void validarCreacion(PostCreateDTO dto) {
        Map<String, String> errores = new LinkedHashMap<>();

        // Título
        if (dto.getTitle() == null || dto.getTitle().trim().length() < 3) {
            errores.put("title", "El título es obligatorio y debe tener al menos 3 caracteres");
        }

        // Contenido mínimo
        boolean textoVacio = dto.getText() == null || dto.getText().trim().isEmpty();
        boolean sinRecursos = (dto.getImageUrl() == null || dto.getImageUrl().trim().isEmpty()) &&
                (dto.getResourceUrl() == null || dto.getResourceUrl().trim().isEmpty());

        if (textoVacio && sinRecursos) {
            errores.put("contenido", "Debe proporcionar al menos texto o una URL de imagen/recurso");
        }

        // Fecha de evento
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDate.now())) {
            errores.put("eventDate", "La fecha del evento no puede ser anterior a hoy");
        }

        // Validación de URLs
        List<String> dominiosBloqueados = Arrays.asList("spamcdn.com", "falso.net");

        if (dto.getImageUrl() != null && contieneDominioBloqueado(dto.getImageUrl(), dominiosBloqueados)) {
            errores.put("imageUrl", "El dominio de la imagen no está permitido");
        }

        if (dto.getResourceUrl() != null && contieneDominioBloqueado(dto.getResourceUrl(), dominiosBloqueados)) {
            errores.put("resourceUrl", "El dominio del recurso no está permitido");
        }

        if (!errores.isEmpty()) {
            throw new BusinessException(errores.toString());
        }
    }

    public void validarActualizacion(PostUpdateDTO dto) {
        Map<String, String> errores = new LinkedHashMap<>();

        if (dto.getTitle() != null && dto.getTitle().trim().length() < 3) {
            errores.put("title", "El título debe tener al menos 3 caracteres");
        }

        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDate.now())) {
            errores.put("eventDate", "La fecha del evento no puede ser anterior a hoy");
        }

        List<String> dominiosBloqueados = Arrays.asList("spamcdn.com", "falso.net");

        if (dto.getImageUrl() != null && contieneDominioBloqueado(dto.getImageUrl(), dominiosBloqueados)) {
            errores.put("imageUrl", "El dominio de la imagen no está permitido");
        }

        if (dto.getResourceUrl() != null && contieneDominioBloqueado(dto.getResourceUrl(), dominiosBloqueados)) {
            errores.put("resourceUrl", "El dominio del recurso no está permitido");
        }

        if (!errores.isEmpty()) {
            throw new BusinessException(errores.toString());
        }
    }

    private boolean contieneDominioBloqueado(String url, List<String> dominiosBloqueados) {
        return dominiosBloqueados.stream().anyMatch(url::contains);
    }

}