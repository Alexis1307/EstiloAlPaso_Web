package spring.estiloAlPaso.business.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.estiloAlPaso.business.api.dto.agencia.AgenciaCreateRequestDto;
import spring.estiloAlPaso.business.api.dto.agencia.AgenciaResponseDto;

import java.io.InputStream;
import java.util.List;

public interface AgenciaService {
    Page<AgenciaResponseDto> buscarAgencias(String search, Pageable pageable);
    List<AgenciaResponseDto> autocompleteAgencias(String search);
    void crearAgencia(AgenciaCreateRequestDto request);
    void importarAgenciasMaestras(InputStream file);
    void actualizarAgenciasDesdeExcel(InputStream file);
}
