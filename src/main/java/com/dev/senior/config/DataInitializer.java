package com.dev.senior.config;

import com.dev.senior.model.*;
import com.dev.senior.repository.ProductoRepository;
import com.dev.senior.repository.UsuarioRepository;
import com.dev.senior.repository.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            VentaRepository ventaRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            logger.info("Inicializando datos demo para la API de Gestión de Ventas...");

            // 1. Crear usuarios iniciales
            Usuario admin = new Usuario("Administrador Principal", "admin@techstore.com", passwordEncoder.encode("Admin123!"), Rol.ADMIN);
            Usuario cliente1 = new Usuario("Juan Pérez", "cliente@techstore.com", passwordEncoder.encode("User123!"), Rol.USER);
            Usuario cliente2 = new Usuario("María González", "maria@techstore.com", passwordEncoder.encode("User123!"), Rol.USER);

            usuarioRepository.save(admin);
            usuarioRepository.save(cliente1);
            usuarioRepository.save(cliente2);

            // 2. Crear productos iniciales
            Producto p1 = new Producto("Laptop Gaming ASUS ROG Strix G16", "Intel i9 13th Gen, 32GB RAM, 1TB SSD, RTX 4070", new BigDecimal("1499.99"), 10, true);
            Producto p2 = new Producto("Smartphone Samsung Galaxy S24 Ultra", "512GB, Pantalla Dynamic AMOLED 2X, Cámara 200MP", new BigDecimal("1199.50"), 15, true);
            Producto p3 = new Producto("Audífonos Sony WH-1000XM5", "Inalámbricos con Cancelación de Ruido Activa", new BigDecimal("349.99"), 25, true);
            Producto p4 = new Producto("Monitor LG UltraGear 27\"", "144Hz 1ms IPS QHD 2K HDR400", new BigDecimal("299.00"), 8, true);
            Producto p5 = new Producto("Teclado Mecánico Keychron K2 V2", "Switches Red Hot-Swappable RGB Inalámbrico", new BigDecimal("99.90"), 30, true);
            Producto p6 = new Producto("Mouse Gamer Logitech G Pro X Superlight", "Sensor HERO 25K Ultra Ligero 63g", new BigDecimal("129.99"), 20, true);
            Producto p7 = new Producto("Consola PlayStation 5 Slim 1TB", "Edición Digital con Control DualSense", new BigDecimal("499.99"), 5, true);
            Producto p8 = new Producto("Smartwatch Apple Watch Series 9 GPS", "45mm Caja de Aluminio Medianoche", new BigDecimal("399.00"), 0, true);

            productoRepository.save(p1);
            productoRepository.save(p2);
            productoRepository.save(p3);
            productoRepository.save(p4);
            productoRepository.save(p5);
            productoRepository.save(p6);
            productoRepository.save(p7);
            productoRepository.save(p8);

            // 3. Crear venta demo inicial
            Venta ventaDemo = new Venta(LocalDateTime.now().minusDays(1), cliente1, BigDecimal.ZERO);
            DetalleVenta d1 = new DetalleVenta(1, p3.getPrecio(), ventaDemo, p3);
            DetalleVenta d2 = new DetalleVenta(2, p5.getPrecio(), ventaDemo, p5);
            
            ventaDemo.addDetalle(d1);
            ventaDemo.addDetalle(d2);
            ventaDemo.calcularTotal();

            ventaRepository.save(ventaDemo);

            logger.info("¡Datos iniciales cargados exitosamente!");
            logger.info("Credenciales demo:");
            logger.info(" - ADMIN: admin@techstore.com / Admin123!");
            logger.info(" - USER:  cliente@techstore.com / User123!");
        }
    }
}
