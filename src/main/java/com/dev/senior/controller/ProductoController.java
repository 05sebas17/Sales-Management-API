package com.dev.senior.controller;

import com.dev.senior.dto.ProductoRequest;
import com.dev.senior.dto.ProductoResponse;
import com.dev.senior.service.ProductoService;
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
@Tag(name = "Productos", description = "Endpoints para la administración y consulta de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping({"/productos", "/api/v1/productos"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear nuevo producto", description = "Permite a un Administrador crear un producto en el sistema")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping({"/productos", "/api/v1/productos"})
    @Operation(summary = "Listar todos los productos", description = "Consulta la lista completa de productos disponibles")
    public ResponseEntity<List<ProductoResponse>> listar() {
        List<ProductoResponse> productos = productoService.listarTodos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping({"/productos/{id}", "/api/v1/productos/{id}"})
    @Operation(summary = "Buscar producto por ID", description = "Obtiene los detalles de un producto por su identificador")
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        ProductoResponse response = productoService.buscarPorIdResponse(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping({"/productos/{id}", "/api/v1/productos/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar producto", description = "Permite a un Administrador modificar los datos de un producto existente")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.actualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping({"/productos/{id}", "/api/v1/productos/{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar producto", description = "Permite a un Administrador eliminar un producto por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
