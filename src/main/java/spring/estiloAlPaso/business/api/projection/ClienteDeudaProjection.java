package spring.estiloAlPaso.business.api.projection;

import java.math.BigDecimal;

public interface ClienteDeudaProjection {
    String getUsuario();
    BigDecimal getDeudaTotal();
}
