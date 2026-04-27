package spring.estiloAlPaso.business.domain.mapper;

import org.mapstruct.Mapper;
import spring.estiloAlPaso.business.api.dto.agencia.AgenciaResponseDto;
import spring.estiloAlPaso.business.data.entity.Agencia;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgenciaMapper {

    AgenciaResponseDto toResponse(Agencia agencia);

    List<AgenciaResponseDto> toResponseList(List<Agencia> agencias);
}
