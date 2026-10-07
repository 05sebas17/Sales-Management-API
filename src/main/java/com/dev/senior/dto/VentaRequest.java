package com.dev.senior.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class VentaRequest {

    @NotEmpty(message = "La venta debe contener al menos un producto")
    @Valid
    private List<DetalleVentaRequest> detalles;

    public VentaRequest() {}

    public VentaRequest(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }

    public List<DetalleVentaRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }
}
