package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.post.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz para operaciones sobre publicaciones en la red social académica.
 * Aplica lógica de negocio con eliminación lógica y control de auditoría.
 */
public interface IPostService {

    // === Crear ===

    /**
     * Crea una nueva publicación para un usuario activo.
     *
     * @param username nombre de usuario autor del post.
     * @param postCreateDTO datos de la nueva publicación.
     * @return DTO de la publicación creada.
     */
    PostDTO crearPost(String username, PostCreateDTO postCreateDTO);

    // === Actualización ===

    /**
     * Actualiza un post propio del usuario autenticado.
     * @param postId ID de la publicación.
     * @param postUpdateDTO nuevos datos.
     * @return DTO actualizado.
     */
    PostDTO actualizarPostPropio(Long postId, PostUpdateDTO postUpdateDTO);

    /**
     * Actualiza un post como administrador.
     * @param postId ID de la publicación.
     * @param postUpdateDTO nuevos datos.
     * @return DTO actualizado.
     */
    PostDTO actualizarPostComoAdmin(Long postId, PostUpdateDTO postUpdateDTO);

    // === Eliminación lógica ===

    /**
     * Elimina lógicamente un post propio.
     * @param postId ID del post.
     * @param motivoBaja motivo de la baja.
     * @return DTO eliminado.
     */
    PostDTO eliminarPostPropio(Long postId, String motivoBaja);

    /**
     * Elimina lógicamente un post como administrador.
     * @param postId ID del post.
     * @param motivoBaja motivo de la baja.
     * @return DTO eliminado.
     */
    PostDTO eliminarPostComoAdmin(Long postId, String motivoBaja);

    // === Lectura ===

    /**
     * Obtiene una publicación activa por ID.
     * @param postId ID de la publicación.
     * @return DTO del post.
     */
    PostDTO obtenerPorId(Long postId);

    /**
     * Lista publicaciones activas de un usuario.
     * @param username autor.
     * @param pageable configuración de paginación.
     * @return Página de publicaciones activas.
     */
    Page<PostDTO> obtenerPostsDeUsuario(String username, Pageable pageable);

    /**
     * Busca publicaciones activas por texto en título o contenido.
     * @param texto texto de búsqueda.
     * @param pageable configuración de paginación.
     * @return Página de resultados.
     */
    Page<PostDTO> buscarPostsPorTexto(String texto, Pageable pageable);

    /**
     * Obtiene las 10 publicaciones más recientes (activas).
     * @return Lista de publicaciones recientes.
     */
    List<PostDTO> obtenerTop10PublicacionesRecientes();
}
