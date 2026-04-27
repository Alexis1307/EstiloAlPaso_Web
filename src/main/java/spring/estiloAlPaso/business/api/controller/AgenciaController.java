package spring.estiloAlPaso.business.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.estiloAlPaso.business.api.dto.agencia.AgenciaResponseDto;
import spring.estiloAlPaso.business.domain.service.AgenciaService;

import java.util.List;

@RestController
@RequestMapping("/agencias")
@AllArgsConstructor
public class AgenciaController {

    private final AgenciaService agenciaService;

    @GetMapping
    public ResponseEntity<Page<AgenciaResponseDto>> buscar(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable
    ) {
        return ResponseEntity.ok(
                agenciaService.buscarAgencias(search, pageable)
        );
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<AgenciaResponseDto>> autpcompleteBuscarAgencias(
            @RequestParam String query
    ) {
        return ResponseEntity.ok(agenciaService.autocompleteAgencias(query));
    }

    @PostMapping("/importar")
    public ResponseEntity<String> importarAgencias(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        try {
            agenciaService.importarAgenciasMaestras(file.getInputStream());
            return ResponseEntity.ok("Importación completada correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al importar: " + e.getMessage());
        }
    }

    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizarAgencias(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        try {
            agenciaService.actualizarAgenciasDesdeExcel(file.getInputStream());
            return ResponseEntity.ok("Importación completada correctamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error al importar: " + e.getMessage());
        }
    }
}
