package spring.estiloAlPaso.business.api.projection;

import java.math.BigDecimal;

public interface DashboardCardsProjection {
    Long getPaquetesActivos();
    Long getEnviosPendientes();
    BigDecimal getMontoTotal();
    BigDecimal getMontoPagado();
    BigDecimal getMontoPendiente();
}
