package com.dev.senior.controller;

import com.dev.senior.dto.VentaRequest;
import com.dev.senior.dto.VentaResponse;
import com.dev.senior.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping
@Tag(name = "Ventas", description = "Endpoints de registro y consulta de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping({"/ventas", "/api/v1/ventas"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Registrar nueva venta", description = "Procesa una compra, actualiza el stock de los productos y asigna la venta al usuario autenticado")
    public ResponseEntity<VentaResponse> registrar(
            @Valid @RequestBody VentaRequest request,
            Principal principal) {
        String correoUsuario = principal.getName();
        VentaResponse response = ventaService.registrarVenta(request, correoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping({"/ventas/mis-ventas", "/api/v1/ventas/mis-ventas"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Consultar mis ventas", description = "Obtiene el historial de compras asociadas únicamente al usuario autenticado")
    public ResponseEntity<List<VentaResponse>> misVentas(Principal principal) {
        String correoUsuario = principal.getName();
        List<VentaResponse> ventas = ventaService.listarPorCorreoUsuario(correoUsuario);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping({"/ventas", "/api/v1/ventas"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todas las ventas (ADMIN)", description = "Obtiene el historial completo de ventas de todo el sistema")
    public ResponseEntity<List<VentaResponse>> listarTodas() {
        List<VentaResponse> ventas = ventaService.listarTodas();
        return ResponseEntity.ok(ventas);
    }

    @GetMapping({"/ventas/{id}", "/api/v1/ventas/{id}"})
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Consultar venta por ID", description = "Obtiene los detalles itemizados de una venta por su ID")
    public ResponseEntity<VentaResponse> buscarPorId(@PathVariable Long id) {
        VentaResponse response = ventaService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }
}
