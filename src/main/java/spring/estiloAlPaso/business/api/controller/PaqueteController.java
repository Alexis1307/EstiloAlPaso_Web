package spring.estiloAlPaso.business.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteDetalleDto;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteResumenDto;
import spring.estiloAlPaso.business.domain.service.PaqueteService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PaqueteController {

    private final PaqueteService paqueteService;

    @GetMapping("/{prendaId}/paquetes")
    public ResponseEntity<List<PaqueteResumenDto>> listarPaquetes(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(paqueteService.listarPorCliente(id));
    }

    @GetMapping("/paquetes/{prendaId}")
    public ResponseEntity<PaqueteDetalleDto> obtenerDetallePaquete(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(paqueteService.obtenerDetalle(id));
    }
}
