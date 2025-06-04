package red_social_academica.red_social_academica.validation;

import org.springframework.stereotype.Component;
import red_social_academica.red_social_academica.dto.comment.CommentCreateDTO;
import red_social_academica.red_social_academica.validation.exception.BusinessException;

@Component
public class CommentValidator {

    public void validarCreacion(CommentCreateDTO dto) {
        validarContenido(dto.getContent());
        validarPostId(dto.getPostId());
    }

    private void validarContenido(String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new BusinessException("El contenido del comentario no puede estar vacío");
        }
        if (contenido.length() < 2 || contenido.length() > 500) {
            throw new BusinessException("El comentario debe tener entre 2 y 500 caracteres");
        }
    }

    private void validarPostId(Long postId) {
        if (postId == null || postId <= 0) {
            throw new BusinessException("El ID del post es obligatorio y debe ser válido");
        }
    }
}