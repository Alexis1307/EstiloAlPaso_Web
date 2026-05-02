package spring.estiloAlPaso.business.api.dto.dashboard;

import java.math.BigDecimal;

public record ClienteDeudaDto(
        String usuarioTiktok,
        BigDecimal deudaTotal
) {}
