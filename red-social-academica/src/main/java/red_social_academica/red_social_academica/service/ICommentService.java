package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.comment.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {

    // === Crear ===

    /**
     * Crea un nuevo comentario en una publicación activa.
     * @param username autor del comentario
     * @param commentCreateDTO contenido del comentario y post destino
     * @return DTO del comentario creado
     */
    CommentDTO crearComentario(String username, CommentCreateDTO commentCreateDTO);

    // === Leer ===

    /**
     * Obtiene los comentarios activos de una publicación (ordenados por fecha).
     * @param postId ID de la publicación
     * @return Lista de comentarios activos
     */
    List<CommentDTO> obtenerComentariosDePost(Long postId);

    /**
     * Obtiene los comentarios activos realizados por un usuario (sin paginar).
     * @param username nombre de usuario
     * @return Lista de comentarios activos del usuario
     */
    List<CommentDTO> obtenerComentariosDeUsuario(String username);

    /**
     * Obtiene los comentarios activos realizados por un usuario (paginados).
     * @param username nombre del autor
     * @param pageable configuración de paginación
     * @return Página de comentarios activos
     */
    Page<CommentDTO> obtenerComentariosDeUsuario(String username, Pageable pageable);

    // === Actualizar ===

    /**
     * Actualiza un comentario propio del usuario autenticado.
     * @param commentId ID del comentario
     * @param commentUpdateDTO contenido actualizado
     * @return DTO actualizado
     */
    CommentDTO actualizarComentarioPropio(Long commentId, CommentUpdateDTO commentUpdateDTO);

    /**
     * Actualiza un comentario como administrador.
     * @param commentId ID del comentario
     * @param commentUpdateDTO contenido actualizado
     * @return DTO actualizado
     */
    CommentDTO actualizarComentarioComoAdmin(Long commentId, CommentUpdateDTO commentUpdateDTO);

    // === Eliminar ===

    /**
     * Elimina lógicamente un comentario propio (público).
     * @param commentId ID del comentario
     * @param motivoBaja motivo de eliminación
     * @return DTO eliminado
     */
    CommentDTO eliminarComentarioPropio(Long commentId, String motivoBaja);

    /**
     * Elimina lógicamente un comentario como administrador.
     * @param commentId ID del comentario
     * @param motivoBaja motivo de eliminación
     * @return DTO eliminado
     */
    CommentDTO eliminarComentarioComoAdmin(Long commentId, String motivoBaja);
}