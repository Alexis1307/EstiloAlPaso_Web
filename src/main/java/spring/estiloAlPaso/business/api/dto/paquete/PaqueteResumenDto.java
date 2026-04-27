package spring.estiloAlPaso.business.api.dto.paquete;

import spring.estiloAlPaso.business.data.entity.Paquete.EstadoPaquete;

import java.math.BigDecimal;

public record PaqueteResumenDto(
        Integer paqueteId,
        EstadoPaquete estado,
        Integer cantidadPrendas,
        BigDecimal total,
        BigDecimal pagado,
        BigDecimal pendiente
) {
}
