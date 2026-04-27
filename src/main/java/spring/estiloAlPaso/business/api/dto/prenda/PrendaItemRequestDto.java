package spring.estiloAlPaso.business.api.dto.prenda;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import spring.estiloAlPaso.business.data.entity.Prenda.EstadoPrenda;

import java.math.BigDecimal;

public record PrendaItemRequestDto(

        @NotBlank(message = "Descripción obligatoria")
        String descripcion,

        @NotNull(message = "Precio total obligatorio")
        @DecimalMin(value = "0.01", message = "Precio total debe ser mayor a 0")
        BigDecimal precioTotal,

        @NotNull(message = "Precio pagado obligatorio")
        @DecimalMin(value = "0.00", message = "Precio pagado no puede ser negativo")
        BigDecimal precioPagado,

        @NotNull(message = "Estado obligatorio")
        EstadoPrenda estado
) {}
