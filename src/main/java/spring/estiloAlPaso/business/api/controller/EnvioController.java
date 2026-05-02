package spring.estiloAlPaso.business.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import spring.estiloAlPaso.business.api.dto.envio.EnvioListadoResponseDto;
import spring.estiloAlPaso.business.api.dto.envio.EnvioResponseDto;
import spring.estiloAlPaso.business.data.entity.Envio.EstadoEnvio;
import spring.estiloAlPaso.business.domain.service.EnvioService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/envios")
@AllArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<String> crearEnvio(@PathVariable Integer clienteId) {
        envioService.crearEnvio(clienteId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Envío creado correctamente");
    }

    @PatchMapping("/{envioId}/cancelar")
    public ResponseEntity<String> cancelarEnvio(@PathVariable Integer envioId) {
        envioService.cancelarEnvio(envioId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Envío cancelado correctamente");
    }

    @PatchMapping("/{envioId}/enviar")
    public ResponseEntity<String> marcarComoEnviado(@PathVariable Integer envioId) {
        envioService.marcarComoEnviado(envioId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Marcado como Enviado correctamente");
    }

    @GetMapping
    public ResponseEntity<EnvioListadoResponseDto> listarEnvios() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @GetMapping("/cliente/{clienteId}/pendiente")
    public ResponseEntity<EnvioResponseDto> obtenerPendiente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(envioService.obtenerPendiente(clienteId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<EnvioResponseDto>> obtenerPorEstado(
            @PathVariable Integer clienteId,
            @RequestParam(required = false) EstadoEnvio estado
    ) {
        return ResponseEntity.ok(envioService.obtenerPorEstado(clienteId, estado));
    }

    @PostMapping("/clave-lote")
    public ResponseEntity<Map<String, String>> generarClaveLote() {

        String clave = envioService.generarClaveParaEncomiendasPendientes();

        return ResponseEntity.ok(
                Map.of("clave", clave)
        );
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> descargarExcel(
            @RequestParam Integer origen
    ) {

        byte[] excel = envioService.generarExcel(origen);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("envios_" + LocalDate.now() + ".xlsx")
                        .build()
        );

        return new ResponseEntity<>(excel, headers, HttpStatus.OK);
    }
}
