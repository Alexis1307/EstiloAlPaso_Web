package spring.estiloAlPaso.business.api.dto.prenda;

import spring.estiloAlPaso.business.data.entity.Prenda.EstadoPrenda;

import java.math.BigDecimal;

public record PrendaResponseDto(
        Integer prendaId,
        String descripcion,
        BigDecimal precioPagado,
        BigDecimal precioTotal,
        EstadoPrenda estado
) {}
