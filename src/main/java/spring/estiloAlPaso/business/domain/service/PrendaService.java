package spring.estiloAlPaso.business.domain.service;

import spring.estiloAlPaso.business.api.dto.prenda.ActualizarPrendaRequestDto;
import spring.estiloAlPaso.business.api.dto.prenda.PrendaResponseDto;
import spring.estiloAlPaso.business.api.dto.prenda.RegistrarPrendasRequestDto;
import spring.estiloAlPaso.business.api.dto.prenda.RegistrarPrendasResponseDto;


public interface PrendaService {

    RegistrarPrendasResponseDto registrarPrendas(RegistrarPrendasRequestDto request);
    RegistrarPrendasResponseDto actualizarPrenda(Integer prendaId, ActualizarPrendaRequestDto request);
    RegistrarPrendasResponseDto eliminarPrenda(Integer prendaId);
    PrendaResponseDto obtenerPrenda(Integer prendaId);
}
