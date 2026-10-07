package com.dev.senior.service;

import com.dev.senior.dto.DetalleVentaRequest;
import com.dev.senior.dto.VentaRequest;
import com.dev.senior.dto.VentaResponse;
import com.dev.senior.exception.BadRequestException;
import com.dev.senior.exception.InsufficientStockException;
import com.dev.senior.exception.ResourceNotFoundException;
import com.dev.senior.model.Producto;
import com.dev.senior.model.Rol;
import com.dev.senior.model.Usuario;
import com.dev.senior.model.Venta;
import com.dev.senior.repository.ProductoRepository;
import com.dev.senior.repository.UsuarioRepository;
import com.dev.senior.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VentaService ventaService;

    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Ana Lopez", "ana@test.com", "password", Rol.USER);
        usuario.setId(1L);

        producto = new Producto("Audífonos Bluetooth", "Excelentes audífonos", new BigDecimal("100.00"), 5, true);
        producto.setId(10L);
    }

    @Test
    void registrarVenta_Exito_DescuentaStockYCalculaTotal() {
        VentaRequest request = new VentaRequest(List.of(new DetalleVentaRequest(10L, 2)));

        when(usuarioRepository.findByCorreo("ana@test.com")).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setId(100L);
            return v;
        });

        VentaResponse response = ventaService.registrarVenta(request, "ana@test.com");

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(1L, response.getUsuarioId());
        assertEquals(new BigDecimal("200.00"), response.getTotal());
        assertEquals(3, producto.getStock()); // 5 - 2 = 3

        verify(productoRepository, times(1)).save(producto);
        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    @Test
    void registrarVenta_StockInsuficiente_LanzaExcepcion() {
        VentaRequest request = new VentaRequest(List.of(new DetalleVentaRequest(10L, 10))); // solicita 10, hay 5

        when(usuarioRepository.findByCorreo("ana@test.com")).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        assertThrows(InsufficientStockException.class, () -> ventaService.registrarVenta(request, "ana@test.com"));
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void registrarVenta_ProductoInactivo_LanzaExcepcion() {
        producto.setEstado(false);
        VentaRequest request = new VentaRequest(List.of(new DetalleVentaRequest(10L, 1)));

        when(usuarioRepository.findByCorreo("ana@test.com")).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));

        assertThrows(BadRequestException.class, () -> ventaService.registrarVenta(request, "ana@test.com"));
    }

    @Test
    void registrarVenta_UsuarioNoExiste_LanzaExcepcion() {
        VentaRequest request = new VentaRequest(List.of(new DetalleVentaRequest(10L, 1)));

        when(usuarioRepository.findByCorreo("desconocido@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ventaService.registrarVenta(request, "desconocido@test.com"));
    }
}
