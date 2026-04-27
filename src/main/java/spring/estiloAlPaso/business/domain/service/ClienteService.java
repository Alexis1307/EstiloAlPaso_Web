package spring.estiloAlPaso.business.domain.service;

import spring.estiloAlPaso.business.api.dto.cliente.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    Page<ClienteListaResponseDto> listarClientes(
            String usuarioTikTok,
            String telefono,
            Pageable pageable);
    ClienteListaResponseDto crearCliente(ClienteCreateRequestDto request);
    ClienteDetalleResponseDto obtenerPorId(Integer id);
    ClienteDetalleResponseDto actualizarCliente(Integer id, ClienteUpdateRequestDto request);
    void eliminarCliente(Integer id);
}
