package com.dev.senior.controller;

import com.dev.senior.dto.AuthRequest;
import com.dev.senior.dto.AuthResponse;
import com.dev.senior.dto.RegisterRequest;
import com.dev.senior.security.UserPrincipal;
import com.dev.senior.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Autenticación", description = "Endpoints de registro e inicio de sesión con JWT")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping({"/auth/register", "/api/v1/auth/register"})
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un usuario en la plataforma y devuelve un token JWT válido")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping({"/auth/login", "/api/v1/auth/login"})
    @Operation(summary = "Iniciar sesión", description = "Autentica credenciales (correo y contraseña) y retorna token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/auth/me", "/api/v1/auth/me"})
    @Operation(summary = "Obtener perfil autenticado", description = "Retorna la información del usuario autenticado actualmente por token JWT")
    public ResponseEntity<Map<String, Object>> me(@AuthenticationPrincipal UserPrincipal currentUser) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", currentUser.getId());
        profile.put("nombre", currentUser.getNombre());
        profile.put("correo", currentUser.getCorreo());
        profile.put("authorities", currentUser.getAuthorities());
        return ResponseEntity.ok(profile);
    }
}
