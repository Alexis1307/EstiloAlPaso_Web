package spring.estiloAlPaso.business.api.dto.cliente;

import lombok.Builder;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteDetalleDto;

@Builder
public record ClienteDetalleResponseDto(
        Integer id,
        String nombreReal,
        String usuarioTikTok,
        String dni,
        String telefono,
        String direccion,
        String agencia,
        String ciudad,

        PaqueteDetalleDto paquete
) {}
