package com.dev.senior.service;

import com.dev.senior.dto.ProductoRequest;
import com.dev.senior.dto.ProductoResponse;
import com.dev.senior.exception.ResourceNotFoundException;
import com.dev.senior.model.Producto;
import com.dev.senior.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Producto producto = new Producto(
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getStock(),
                (request.getEstado() != null) ? request.getEstado() : true
        );
        Producto guardado = productoRepository.save(producto);
        return mapToResponse(guardado);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        if (request.getEstado() != null) {
            producto.setEstado(request.getEstado());
        }

        Producto actualizado = productoRepository.save(producto);
        return mapToResponse(actualizado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarDisponibles() {
        return productoRepository.findByEstadoTrue().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse buscarPorIdResponse(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return mapToResponse(producto);
    }

    @Transactional(readOnly = true)
    public Producto buscarEntityPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    public ProductoResponse mapToResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getEstado()
        );
    }
}
