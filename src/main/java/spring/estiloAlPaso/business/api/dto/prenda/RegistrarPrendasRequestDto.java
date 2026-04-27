package spring.estiloAlPaso.business.api.dto.prenda;

import java.util.List;

public record RegistrarPrendasRequestDto(
        Integer clienteId,
        String usuarioTikTok,
        String telefono,

        List<PrendaItemRequestDto> prendas
) {
}
