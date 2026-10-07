package com.dev.senior.service;

import com.dev.senior.dto.DetalleVentaRequest;
import com.dev.senior.dto.DetalleVentaResponse;
import com.dev.senior.dto.VentaRequest;
import com.dev.senior.dto.VentaResponse;
import com.dev.senior.exception.BadRequestException;
import com.dev.senior.exception.InsufficientStockException;
import com.dev.senior.exception.ResourceNotFoundException;
import com.dev.senior.model.DetalleVenta;
import com.dev.senior.model.Producto;
import com.dev.senior.model.Usuario;
import com.dev.senior.model.Venta;
import com.dev.senior.repository.ProductoRepository;
import com.dev.senior.repository.UsuarioRepository;
import com.dev.senior.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public VentaService(
            VentaRepository ventaRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public VentaResponse registrarVenta(VentaRequest request, String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoUsuario));

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new BadRequestException("La venta debe contener al menos un producto");
        }

        Venta venta = new Venta(LocalDateTime.now(), usuario, BigDecimal.ZERO);

        for (DetalleVentaRequest detalleReq : request.getDetalles()) {
            Producto producto = productoRepository.findById(detalleReq.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalleReq.getProductoId()));

            if (Boolean.FALSE.equals(producto.getEstado())) {
                throw new BadRequestException("El producto '" + producto.getNombre() + "' no se encuentra disponible para la venta");
            }

            if (producto.getStock() < detalleReq.getCantidad()) {
                throw new InsufficientStockException("Stock insuficiente para el producto '" + producto.getNombre() +
                        "'. Disponible: " + producto.getStock() + ", Solicitado: " + detalleReq.getCantidad());
            }

            // Descontar stock del producto
            producto.setStock(producto.getStock() - detalleReq.getCantidad());
            productoRepository.save(producto);

            // Crear detalle de venta
            DetalleVenta detalle = new DetalleVenta(
                    detalleReq.getCantidad(),
                    producto.getPrecio(),
                    venta,
                    producto
            );
            venta.addDetalle(detalle);
        }

        // Calcular total acumulado de la venta
        venta.calcularTotal();

        Venta ventaGuardada = ventaRepository.save(venta);
        return mapToResponse(ventaGuardada);
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> listarTodas() {
        return ventaRepository.findAllByOrderByFechaDesc().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId);
        }
        return ventaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> listarPorCorreoUsuario(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correoUsuario));

        return ventaRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponse buscarPorIdResponse(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
        return mapToResponse(venta);
    }

    public VentaResponse mapToResponse(Venta venta) {
        List<DetalleVentaResponse> detallesResponse = venta.getDetalles().stream()
                .map(d -> new DetalleVentaResponse(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getSubtotal()
                ))
                .toList();

        return new VentaResponse(
                venta.getId(),
                venta.getUsuario().getId(),
                venta.getUsuario().getNombre(),
                venta.getUsuario().getCorreo(),
                venta.getFecha(),
                venta.getTotal(),
                detallesResponse
        );
    }
}
