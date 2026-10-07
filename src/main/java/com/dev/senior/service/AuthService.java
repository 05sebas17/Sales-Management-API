package com.dev.senior.service;

import com.dev.senior.dto.AuthRequest;
import com.dev.senior.dto.AuthResponse;
import com.dev.senior.dto.RegisterRequest;
import com.dev.senior.exception.DuplicateResourceException;
import com.dev.senior.model.Rol;
import com.dev.senior.model.Usuario;
import com.dev.senior.repository.UsuarioRepository;
import com.dev.senior.security.JwtUtils;
import com.dev.senior.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con el correo: " + request.getCorreo());
        }

        Rol rolAsignado = (request.getRol() != null) ? request.getRol() : Rol.USER;

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getCorreo(),
                passwordEncoder.encode(request.getContraseña()),
                rolAsignado
        );

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        return new AuthResponse(
                token,
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getCorreo(),
                usuarioGuardado.getRol().name()
        );
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return new AuthResponse(
                token,
                userPrincipal.getId(),
                userPrincipal.getNombre(),
                userPrincipal.getCorreo(),
                userPrincipal.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "")
        );
    }
}
