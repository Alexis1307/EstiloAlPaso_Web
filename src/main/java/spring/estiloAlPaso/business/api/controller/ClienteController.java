package spring.estiloAlPaso.business.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.estiloAlPaso.business.api.dto.cliente.*;
import spring.estiloAlPaso.business.domain.service.ClienteService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<Page<ClienteListaResponseDto>> listarClientes(
            @RequestParam(required = false) String usuarioTikTok,
            @RequestParam(required = false) String telefono,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                clienteService.listarClientes(usuarioTikTok, telefono, pageable)
        );
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearCliente(
            @Valid @RequestBody ClienteCreateRequestDto request
    ) {

        ClienteListaResponseDto res = clienteService.crearCliente(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", res.id(),
                        "mensaje", "Cliente creado correctamente"
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDetalleResponseDto> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteDetalleResponseDto> actualizarParcial(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteUpdateRequestDto request
    ) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
