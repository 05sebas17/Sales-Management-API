package com.dev.senior.service;

import com.dev.senior.dto.ProductoRequest;
import com.dev.senior.dto.ProductoResponse;
import com.dev.senior.exception.ResourceNotFoundException;
import com.dev.senior.model.Producto;
import com.dev.senior.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto("Teclado Mecánico", "Teclado RGB", new BigDecimal("99.99"), 10, true);
        producto.setId(1L);
    }

    @Test
    void crearProducto_Exito() {
        ProductoRequest request = new ProductoRequest("Teclado Mecánico", "Teclado RGB", new BigDecimal("99.99"), 10, true);
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponse response = productoService.crear(request);

        assertNotNull(response);
        assertEquals("Teclado Mecánico", response.getNombre());
        assertEquals(new BigDecimal("99.99"), response.getPrecio());
        assertEquals(10, response.getStock());
    }

    @Test
    void actualizarProducto_Exito() {
        ProductoRequest updateRequest = new ProductoRequest("Teclado PRO", "Teclado RGB PRO", new BigDecimal("129.99"), 15, true);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponse response = productoService.actualizar(1L, updateRequest);

        assertNotNull(response);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void buscarPorId_NoExiste_LanzaExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.buscarPorIdResponse(99L));
    }

    @Test
    void listarDisponibles_RetornaSoloActivos() {
        when(productoRepository.findByEstadoTrue()).thenReturn(List.of(producto));

        List<ProductoResponse> lista = productoService.listarDisponibles();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }
}
