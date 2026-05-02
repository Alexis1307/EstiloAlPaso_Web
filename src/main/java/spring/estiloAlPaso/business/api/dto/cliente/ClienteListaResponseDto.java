package spring.estiloAlPaso.business.api.dto.cliente;

import lombok.Builder;
import spring.estiloAlPaso.business.data.entity.Agencia;

@Builder
public record ClienteListaResponseDto(
        Integer id,
        String usuarioTikTok,
        String telefono,
        String ciudad,
        String direccion,
        Agencia agencia,
        EstadoClienteLista estado
) {
}
