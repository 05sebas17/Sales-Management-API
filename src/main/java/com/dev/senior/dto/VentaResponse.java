package com.dev.senior.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class VentaResponse {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioCorreo;
    private LocalDateTime fecha;
    private BigDecimal total;
    private List<DetalleVentaResponse> detalles;

    public VentaResponse() {}

    public VentaResponse(Long id, Long usuarioId, String usuarioNombre, String usuarioCorreo, LocalDateTime fecha, BigDecimal total, List<DetalleVentaResponse> detalles) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioCorreo = usuarioCorreo;
        this.fecha = fecha;
        this.total = total;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioCorreo() {
        return usuarioCorreo;
    }

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<DetalleVentaResponse> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaResponse> detalles) {
        this.detalles = detalles;
    }
}
