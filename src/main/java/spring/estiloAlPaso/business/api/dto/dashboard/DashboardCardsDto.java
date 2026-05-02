package spring.estiloAlPaso.business.api.dto.dashboard;

import java.math.BigDecimal;

public record DashboardCardsDto(
        Long paquetesActivos,
        Long enviosPendientes,
        BigDecimal montoTotal,
        BigDecimal montoPagado,
        BigDecimal montoPendiente
) {
}
