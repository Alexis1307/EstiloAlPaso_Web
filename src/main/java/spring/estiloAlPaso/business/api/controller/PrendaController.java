package spring.estiloAlPaso.business.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.estiloAlPaso.business.api.dto.prenda.ActualizarPrendaRequestDto;
import spring.estiloAlPaso.business.api.dto.prenda.PrendaResponseDto;
import spring.estiloAlPaso.business.api.dto.prenda.RegistrarPrendasRequestDto;
import spring.estiloAlPaso.business.api.dto.prenda.RegistrarPrendasResponseDto;
import spring.estiloAlPaso.business.domain.service.PrendaService;

@RestController
@RequestMapping("/prendas")
@RequiredArgsConstructor
public class PrendaController {

    private final PrendaService prendaService;

    @PostMapping("/registrar")
    public ResponseEntity<RegistrarPrendasResponseDto> registrarPrendas(
            @Valid @RequestBody RegistrarPrendasRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prendaService.registrarPrendas(request));
    }

    @PatchMapping("/{prendaId}")
    public ResponseEntity<RegistrarPrendasResponseDto> actualizar(
            @PathVariable("prendaId") Integer prendaId,
            @RequestBody ActualizarPrendaRequestDto request
    ) {
        return ResponseEntity.ok(prendaService.actualizarPrenda(prendaId, request));
    }

    @DeleteMapping("/{prendaId}")
    public ResponseEntity<RegistrarPrendasResponseDto> eliminar(
            @PathVariable("prendaId") Integer prendaId
    ) {
        return ResponseEntity.ok(prendaService.eliminarPrenda(prendaId));
    }

    @GetMapping("/{prendaId}")
    public ResponseEntity<PrendaResponseDto> obtener(@PathVariable("prendaId") Integer prendaId) {
        return ResponseEntity.ok(prendaService.obtenerPrenda(prendaId));
    }
}
