package spring.estiloAlPaso.business.api.dto.envio;

import java.util.List;

public record EnvioListadoResponseDto(
        List<EnvioResponseDto> delivery,
        List<EnvioResponseDto> encomienda
) {
}
