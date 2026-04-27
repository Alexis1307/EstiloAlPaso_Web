package spring.estiloAlPaso.business.api.dto.cliente;

import jakarta.validation.constraints.Pattern;

public record ClienteUpdateRequestDto(

        String usuarioTikTok,

        String nombreReal,

        @Pattern(regexp = "\\d{8}", message = "DNI debe tener 8 dígitos")
        String dni,

        @Pattern(regexp = "\\d{9}", message = "Teléfono debe tener 9 dígitos")
        String telefono,

        String direccion,
        Integer agenciaId,

        String ciudad
) {}
