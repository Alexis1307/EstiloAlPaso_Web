package spring.estiloAlPaso.business.domain.service;

import spring.estiloAlPaso.business.api.dto.paquete.PaqueteDetalleDto;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteResumenDto;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;

import java.util.List;

public interface PaqueteService {
    Paquete obtenerOCrearPaqueteActivo(Cliente cliente);
    List<PaqueteResumenDto> listarPorCliente(Integer clienteId);
    PaqueteDetalleDto obtenerDetalle(Integer paqueteId);
}
