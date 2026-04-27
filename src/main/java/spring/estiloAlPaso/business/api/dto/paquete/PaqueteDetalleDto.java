package spring.estiloAlPaso.business.api.dto.paquete;

import spring.estiloAlPaso.business.api.dto.prenda.PrendaResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record PaqueteDetalleDto(
        Integer cantidadPrendas,
        BigDecimal total,
        BigDecimal pagado,
        BigDecimal pendiente,
        List<PrendaResponseDto> prendas
) {
}
