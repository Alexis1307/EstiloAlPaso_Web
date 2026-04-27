package spring.estiloAlPaso.business.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.estiloAlPaso.business.api.dto.prenda.RegistrarPrendasResponseDto;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.domain.util.PaqueteCalculator;

import java.math.BigDecimal;


@Mapper(componentModel = "spring")
public interface PaqueteMapper {

    @Mapping(target = "cantidadPrendas", expression = "java(paquete.getPrendas().size())")
    @Mapping(target = "total", expression = "java(calcularTotal(paquete))")
    @Mapping(target = "pagado", expression = "java(calcularPagado(paquete))")
    @Mapping(target = "pendiente", expression = "java(calcularPendiente(paquete))")
    @Mapping(target = "clienteId", expression = "java(paquete.getCliente().getId())")
    @Mapping(target = "usuarioTikTok", expression = "java(paquete.getCliente().getUsuarioTikTok())")
    @Mapping(target = "paqueteId", source = "id")
    RegistrarPrendasResponseDto toRegistrarResponse(Paquete paquete);

    default BigDecimal calcularTotal(Paquete paquete) {
        return PaqueteCalculator.calcularTotal(paquete);
    }

    default BigDecimal calcularPagado(Paquete paquete) {
        return PaqueteCalculator.calcularPagado(paquete);
    }

    default BigDecimal calcularPendiente(Paquete paquete) {
        return PaqueteCalculator.calcularPendiente(paquete);
    }
}
