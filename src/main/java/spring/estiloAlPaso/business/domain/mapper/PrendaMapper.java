package spring.estiloAlPaso.business.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.estiloAlPaso.business.api.dto.prenda.PrendaItemRequestDto;
import spring.estiloAlPaso.business.api.dto.prenda.PrendaResponseDto;
import spring.estiloAlPaso.business.api.dto.prenda.RegistrarPrendasResponseDto;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrendaMapper {
    @Mapping(target = "prendaId", source = "id")
    PrendaResponseDto toResponseDto(Prenda prenda);
}
