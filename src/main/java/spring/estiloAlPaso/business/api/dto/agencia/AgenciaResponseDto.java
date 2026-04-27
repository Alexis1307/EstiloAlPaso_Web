package spring.estiloAlPaso.business.api.dto.agencia;

import lombok.Builder;

@Builder
public record AgenciaResponseDto(
        Integer id,
        String nombre
) {}
