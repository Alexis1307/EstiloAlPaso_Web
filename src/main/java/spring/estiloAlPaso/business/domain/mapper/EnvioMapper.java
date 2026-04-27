package spring.estiloAlPaso.business.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.estiloAlPaso.business.api.dto.cliente.EstadoClienteLista;
import spring.estiloAlPaso.business.api.dto.envio.EnvioResponseDto;
import spring.estiloAlPaso.business.data.entity.Envio.Envio;
import spring.estiloAlPaso.business.domain.util.EnvioCalculator;

import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface EnvioMapper {

    @Mapping(target = "cantidadPrendas",
            expression = "java(envio.getPaquete().getPrendas().size())")

    @Mapping(target = "total", expression = "java(mapTotal(envio))")
    @Mapping(target = "pagado", expression = "java(mapPagado(envio))")
    @Mapping(target = "estadoCliente", expression = "java(mapEstadoCliente(envio))")

    @Mapping(target = "usuarioTikTok", source = "paquete.cliente.usuarioTikTok")
    @Mapping(target = "nombreReal", source = "paquete.cliente.nombreReal")
    @Mapping(target = "telefono", source = "paquete.cliente.telefono")
    @Mapping(target = "direccion", source = "paquete.cliente.direccion")
    @Mapping(target = "ciudad", source = "paquete.cliente.ciudad")

    @Mapping(target = "envioId", source = "id")
    EnvioResponseDto toResponse(Envio envio);

    default BigDecimal mapTotal(Envio envio) {
        return EnvioCalculator.calcularTotal(envio.getPaquete());
    }

    default BigDecimal mapPagado(Envio envio) {
        return EnvioCalculator.calcularPagado(envio.getPaquete());
    }

    default EstadoClienteLista mapEstadoCliente(Envio envio) {
        return EnvioCalculator.calcularEstadoCliente(envio.getPaquete());
    }
}
