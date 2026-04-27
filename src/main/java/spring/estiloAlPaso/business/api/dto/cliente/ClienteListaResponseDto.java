package spring.estiloAlPaso.business.api.dto.cliente;

import lombok.Builder;

@Builder
public record ClienteListaResponseDto(
        Integer id,
        String usuarioTikTok,
        String telefono,
        String direccion,
        EstadoClienteLista estado
) {
}
