package spring.estiloAlPaso.business.api.dto.agencia;

import lombok.Builder;

@Builder
public record AgenciaCreateRequestDto(
        String nombre
) {
}
