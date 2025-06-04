package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.dto.comment.*;
import red_social_academica.red_social_academica.model.Comment;
import red_social_academica.red_social_academica.model.Post;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.CommentRepository;
import red_social_academica.red_social_academica.repository.PostRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.ICommentService;
import red_social_academica.red_social_academica.validation.CommentValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentValidator commentValidator;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                               UserRepository userRepository, CommentValidator commentValidator) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentValidator = commentValidator;
    }

    @Override
    @Transactional
    @CachePut(value = "comentario", key = "#result.id")
    @CacheEvict(value = {"comentariosDePost", "comentariosDeUsuario"}, allEntries = true)
    public CommentDTO crearComentario(String username, CommentCreateDTO dto) {
        commentValidator.validarCreacion(dto);

        User author = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        Post post = postRepository.findByIdAndActivoTrue(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o inactiva"));

        Comment comment = mapFromCreateDTO(dto, author, post);
        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @CachePut(value = "comentario", key = "#commentId")
    @CacheEvict(value = {"comentariosDePost", "comentariosDeUsuario"}, allEntries = true)
    public CommentDTO actualizarComentarioPropio(Long commentId, CommentUpdateDTO dto) {
        String actual = getCurrentUsername();

        Comment comment = commentRepository.findByIdAndActivoTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado o inactivo"));

        if (!comment.getAuthor().getUsername().equals(actual)) {
            throw new SecurityException("No puedes editar este comentario");
        }

        updateEntityFromUpdateDTO(dto, comment, actual);
        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @CachePut(value = "comentario", key = "#commentId")
    @CacheEvict(value = {"comentariosDePost", "comentariosDeUsuario"}, allEntries = true)
    public CommentDTO actualizarComentarioComoAdmin(Long commentId, CommentUpdateDTO dto) {
        if (!isAdmin()) {
            throw new SecurityException("Acceso restringido a administradores");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        updateEntityFromUpdateDTO(dto, comment, getCurrentUsername());
        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"comentario", "comentariosDePost", "comentariosDeUsuario"}, allEntries = true)
    public CommentDTO eliminarComentarioPropio(Long commentId, String motivoBaja) {
        String actual = getCurrentUsername();

        Comment comment = commentRepository.findByIdAndActivoTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado o ya eliminado"));

        if (!comment.getAuthor().getUsername().equals(actual)) {
            throw new SecurityException("No puedes eliminar este comentario");
        }

        marcarComoEliminado(comment, actual, motivoBaja);
        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"comentario", "comentariosDePost", "comentariosDeUsuario"}, allEntries = true)
    public CommentDTO eliminarComentarioComoAdmin(Long commentId, String motivoBaja) {
        if (!isAdmin()) {
            throw new SecurityException("Solo administradores pueden eliminar comentarios ajenos");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        marcarComoEliminado(comment, getCurrentUsername(), motivoBaja);
        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    @Cacheable(value = "comentariosDePost", key = "#postId")
    public List<CommentDTO> obtenerComentariosDePost(Long postId) {
        return commentRepository.findByPostIdAndActivoTrueOrderByCreatedAtAsc(postId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "comentariosDeUsuario", key = "#username")
    public List<CommentDTO> obtenerComentariosDeUsuario(String username) {
        return commentRepository.findByAuthorUsernameAndActivoTrue(username).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Cacheable(value = "comentariosDeUsuarioPaginado", key = "#username + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CommentDTO> obtenerComentariosDeUsuario(String username, Pageable pageable) {
        return commentRepository.findByAuthorUsernameAndActivoTrue(username, pageable)
                .map(this::convertToDTO);
    }

    // ---------- MAPEOS ----------

    private CommentDTO convertToDTO(Comment comment) {
        String fullName = comment.getAuthor().getName() + " " + comment.getAuthor().getLastName();
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorFullName(fullName)
                .postId(comment.getPost().getId())
                .fechaAlta(comment.getFechaAlta())
                .fechaModificacion(comment.getFechaModificacion())
                .fechaBaja(comment.getFechaBaja())
                .motivoBaja(comment.getMotivoBaja())
                .activo(comment.isActivo())
                .build();
    }

    private Comment mapFromCreateDTO(CommentCreateDTO dto, User author, Post post) {
        return Comment.builder()
                .content(dto.getContent())
                .createdAt(new Date())
                .fechaAlta(LocalDate.now())
                .usuarioAlta(author.getUsername())
                .activo(true)
                .author(author)
                .post(post)
                .build();
    }

    private void updateEntityFromUpdateDTO(CommentUpdateDTO dto, Comment comment, String username) {
        comment.setContent(dto.getContent());
        comment.setFechaModificacion(LocalDate.now());
        comment.setUsuarioModificacion(username);
    }

    private void marcarComoEliminado(Comment comment, String usuario, String motivo) {
        comment.setActivo(false);
        comment.setFechaBaja(LocalDate.now());
        comment.setUsuarioBaja(usuario);
        comment.setMotivoBaja(motivo);
    }

    // ---------- Seguridad ----------

    private String getCurrentUsername() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }

    private boolean isAdmin() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
