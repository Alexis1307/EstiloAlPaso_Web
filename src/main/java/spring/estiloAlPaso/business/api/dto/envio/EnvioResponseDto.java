package spring.estiloAlPaso.business.api.dto.envio;

import lombok.Builder;
import spring.estiloAlPaso.business.api.dto.cliente.EstadoClienteLista;
import spring.estiloAlPaso.business.data.entity.Envio.EstadoEnvio;
import spring.estiloAlPaso.business.data.entity.Envio.TipoEnvio;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record EnvioResponseDto(

        Integer envioId,

        String usuarioTikTok,
        String nombreReal,
        String telefono,
        String direccion,
        String ciudad,

        Integer cantidadPrendas,
        BigDecimal total,
        BigDecimal pagado,

        EstadoClienteLista estadoCliente,

        TipoEnvio tipoEnvio,
        EstadoEnvio estadoEnvio,

        String clave,

        Date fechaCreacion,
        Date fechaEnvio
) {}
