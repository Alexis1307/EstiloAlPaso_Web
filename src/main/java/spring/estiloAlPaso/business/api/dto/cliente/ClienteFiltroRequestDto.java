package spring.estiloAlPaso.business.api.dto.cliente;

import lombok.Builder;

@Builder
public record ClienteFiltroRequestDto(
        String usuarioTikTok,
        String telefono,
        EstadoClienteLista estado
) {
}
