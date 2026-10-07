package com.dev.senior.repository;

import com.dev.senior.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    List<Venta> findAllByOrderByFechaDesc();
}
