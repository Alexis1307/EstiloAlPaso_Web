package spring.estiloAlPaso.business.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import spring.estiloAlPaso.business.api.dto.cliente.ClienteCreateRequestDto;
import spring.estiloAlPaso.business.api.dto.cliente.ClienteDetalleResponseDto;
import spring.estiloAlPaso.business.api.dto.cliente.ClienteListaResponseDto;
import spring.estiloAlPaso.business.api.dto.cliente.ClienteUpdateRequestDto;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "estado", ignore = true)
    ClienteListaResponseDto toListaDto(Cliente cliente);

    @Mapping(target = "agencia",
            expression = "java(cliente.getAgencia() != null ? cliente.getAgencia().getNombre() : null)")
    ClienteDetalleResponseDto toDetalleDto(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "paquetes", ignore = true)
    Cliente toEntity(ClienteCreateRequestDto request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "paquetes", ignore = true)
    void updateEntity(ClienteUpdateRequestDto request,
                      @MappingTarget Cliente cliente);
}