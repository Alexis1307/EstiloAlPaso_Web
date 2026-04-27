package spring.estiloAlPaso.business.domain.service;

import spring.estiloAlPaso.business.api.dto.envio.EnvioListadoResponseDto;
import spring.estiloAlPaso.business.api.dto.envio.EnvioResponseDto;
import spring.estiloAlPaso.business.data.entity.Envio.EstadoEnvio;

import java.util.List;

public interface EnvioService {
    void crearEnvio(Integer clienteId);

    void cancelarEnvio(Integer envioId);

    void marcarComoEnviado(Integer envioId);

    EnvioListadoResponseDto listarEnvios();

    EnvioResponseDto obtenerPendiente(Integer clienteId);

    List<EnvioResponseDto> obtenerPorEstado(Integer clienteId, EstadoEnvio estado);

    String generarClaveParaEncomiendasPendientes();

    byte[] generarExcel(Integer origenId);
}
