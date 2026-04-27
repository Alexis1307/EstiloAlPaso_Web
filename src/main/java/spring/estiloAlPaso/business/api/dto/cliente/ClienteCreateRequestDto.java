package spring.estiloAlPaso.business.api.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteCreateRequestDto(
        @NotBlank(message = "Usuario TikTok es obligatorio")
        String usuarioTikTok,

        String nombreReal,

        @Pattern(regexp = "^$|\\d{8}", message = "DNI debe tener exactamente 8 dígitos")
        String dni,

        @NotBlank(message = "Teléfono es obligatorio")
        @Pattern(regexp = "\\d{9}", message = "Teléfono debe tener exactamente 9 dígitos")
        String telefono,

        String direccion,
        Integer agenciaId,
        String ciudad
) {
}
