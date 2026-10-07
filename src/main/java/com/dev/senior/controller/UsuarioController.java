package com.dev.senior.controller;

import com.dev.senior.dto.UsuarioRequest;
import com.dev.senior.dto.UsuarioResponse;
import com.dev.senior.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Usuarios", description = "Endpoints de administración de usuarios (Exclusivo ROL ADMIN)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping({"/usuarios", "/api/v1/usuarios"})
    @Operation(summary = "Crear nuevo usuario (ADMIN)", description = "Permite a un Administrador crear usuarios en el sistema")
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping({"/usuarios", "/api/v1/usuarios"})
    @Operation(summary = "Listar todos los usuarios (ADMIN)", description = "Obtiene la lista completa de usuarios registrados")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping({"/usuarios/{id}", "/api/v1/usuarios/{id}"})
    @Operation(summary = "Buscar usuario por ID (ADMIN)", description = "Consulta los detalles de un usuario específico")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse response = usuarioService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping({"/usuarios/{id}", "/api/v1/usuarios/{id}"})
    @Operation(summary = "Eliminar usuario por ID (ADMIN)", description = "Elimina un usuario del sistema por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
