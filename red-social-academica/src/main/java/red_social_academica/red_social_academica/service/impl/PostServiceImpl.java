package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.dto.post.*;
import red_social_academica.red_social_academica.model.Post;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.PostRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IPostService;
import red_social_academica.red_social_academica.validation.PostValidator;
import static red_social_academica.red_social_academica.auth.security.AuthUtils.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostValidator postValidator;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // === CREAR ===

    @Override
    @Transactional
    @CacheEvict(value = {"postPorId", "top10Posts"}, allEntries = true)
    public PostDTO crearPost(String username, PostCreateDTO dto) {
        postValidator.validarCreacion(dto);
        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        Post post = mapFromCreateDTO(dto, user);
        return convertToDTO(postRepository.save(post));
    }

    // === ACTUALIZAR PUBLICO ===

    @Override
    @Transactional
    @CachePut(value = "postPorId", key = "#postId")
    @CacheEvict(value = "top10Posts", allEntries = true)
    public PostDTO actualizarPostPropio(Long postId, PostUpdateDTO dto) {
        String actual = getCurrentUsername();
        Post post = postRepository.findByIdAndActivoTrue(postId)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o inactiva"));

        if (!post.getUser().getUsername().equals(actual)) {
            throw new SecurityException("No puedes modificar publicaciones de otros usuarios");
        }

        postValidator.validarActualizacion(dto);
        updateEntityFromUpdateDTO(dto, post);
        post.setFechaModificacion(LocalDate.now());
        post.setUsuarioModificacion(actual);

        return convertToDTO(postRepository.save(post));
    }

    // === ACTUALIZAR ADMIN ===

    @Override
    @Transactional
    @CachePut(value = "postPorId", key = "#postId")
    @CacheEvict(value = "top10Posts", allEntries = true)
    public PostDTO actualizarPostComoAdmin(Long postId, PostUpdateDTO dto) {
        if (!isAdmin()) {
            throw new SecurityException("Solo administradores pueden editar cualquier post");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        postValidator.validarActualizacion(dto);
        updateEntityFromUpdateDTO(dto, post);
        post.setFechaModificacion(LocalDate.now());
        post.setUsuarioModificacion(getCurrentUsername());

        return convertToDTO(postRepository.save(post));
    }

    // === ELIMINAR PUBLICO ===

    @Override
    @Transactional
    @CacheEvict(value = {"postPorId", "top10Posts"}, key = "#postId", allEntries = true)
    public PostDTO eliminarPostPropio(Long postId, String motivo) {
        String actual = getCurrentUsername();
        Post post = postRepository.findByIdAndActivoTrue(postId)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o ya eliminada"));

        if (!post.getUser().getUsername().equals(actual)) {
            throw new SecurityException("No puedes eliminar publicaciones de otros usuarios");
        }

        post.setActivo(false);
        post.setFechaBaja(LocalDate.now());
        post.setUsuarioBaja(actual);
        post.setMotivoBaja(motivo);

        return convertToDTO(postRepository.save(post));
    }

    // === ELIMINAR ADMIN ===

    @Override
    @Transactional
    @CacheEvict(value = {"postPorId", "top10Posts"}, key = "#postId", allEntries = true)
    public PostDTO eliminarPostComoAdmin(Long postId, String motivo) {
        if (!isAdmin()) {
            throw new SecurityException("Solo administradores pueden eliminar cualquier post");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        post.setActivo(false);
        post.setFechaBaja(LocalDate.now());
        post.setUsuarioBaja(getCurrentUsername());
        post.setMotivoBaja(motivo);

        return convertToDTO(postRepository.save(post));
    }

    // === CONSULTAS ===

    @Override
    @Cacheable(value = "postPorId", key = "#postId")
    public PostDTO obtenerPorId(Long postId) {
        Post post = postRepository.findByIdAndActivoTrue(postId)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o inactiva"));
        return convertToDTO(post);
    }

    @Override
    public Page<PostDTO> obtenerPostsDeUsuario(String username, Pageable pageable) {
        return postRepository.findByUserUsernameAndActivoTrue(username, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<PostDTO> buscarPostsPorTexto(String texto, Pageable pageable) {
        return postRepository.searchByTitleOrText(texto, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Cacheable(value = "top10Posts")
    public List<PostDTO> obtenerTop10PublicacionesRecientes() {
        return postRepository.findTop10ByActivoTrueOrderByDateDesc().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // === MAPEOS ===

    private PostDTO convertToDTO(Post post) {
        String fullName = post.getUser().getName() + " " + post.getUser().getLastName();
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .imageUrl(post.getImageUrl())
                .resourceUrl(post.getResourceUrl())
                .eventDate(post.getEventDate())
                .date(post.getDate())
                .userId(post.getUser().getId())
                .authorFullName(fullName)
                .fechaAlta(post.getFechaAlta())
                .fechaModificacion(post.getFechaModificacion())
                .fechaBaja(post.getFechaBaja())
                .motivoBaja(post.getMotivoBaja())
                .activo(post.isActivo())
                .build();
    }

    private Post mapFromCreateDTO(PostCreateDTO dto, User user) {
        return Post.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .imageUrl(dto.getImageUrl())
                .resourceUrl(dto.getResourceUrl())
                .eventDate(dto.getEventDate())
                .date(new Date())
                .fechaAlta(LocalDate.now())
                .usuarioAlta(user.getUsername())
                .activo(true)
                .user(user)
                .build();
    }

    private void updateEntityFromUpdateDTO(PostUpdateDTO dto, Post post) {
        if (dto.getTitle() != null)
            post.setTitle(dto.getTitle());
        if (dto.getText() != null)
            post.setText(dto.getText());
        if (dto.getImageUrl() != null)
            post.setImageUrl(dto.getImageUrl());
        if (dto.getResourceUrl() != null)
            post.setResourceUrl(dto.getResourceUrl());
        if (dto.getEventDate() != null)
            post.setEventDate(dto.getEventDate());
    }
}
