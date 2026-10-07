package com.dev.senior.service;

import com.dev.senior.dto.UsuarioRequest;
import com.dev.senior.dto.UsuarioResponse;
import com.dev.senior.exception.DuplicateResourceException;
import com.dev.senior.exception.ResourceNotFoundException;
import com.dev.senior.model.Rol;
import com.dev.senior.model.Usuario;
import com.dev.senior.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        Rol rolAsignado = (request.getRol() != null) ? request.getRol() : Rol.USER;

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getCorreo(),
                passwordEncoder.encode(request.getContrasena()),
                rolAsignado
        );

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorIdResponse(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return mapToResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntityPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntityPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public UsuarioResponse mapToResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().name()
        );
    }
}
