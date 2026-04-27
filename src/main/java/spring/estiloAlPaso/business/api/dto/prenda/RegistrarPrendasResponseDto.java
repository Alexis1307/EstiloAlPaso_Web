package spring.estiloAlPaso.business.api.dto.prenda;

import java.math.BigDecimal;

public record RegistrarPrendasResponseDto(
        Integer clienteId,
        String usuarioTikTok,

        Integer cantidadPrendas,

        BigDecimal total,
        BigDecimal pagado,
        BigDecimal pendiente,

        Integer paqueteId
) {
}
