package spring.estiloAlPaso.business.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.cliente.*;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteDetalleDto;
import spring.estiloAlPaso.business.api.dto.prenda.PrendaResponseDto;
import spring.estiloAlPaso.business.api.exeptions.BusinessException;
import spring.estiloAlPaso.business.api.exeptions.NotFoundException;
import spring.estiloAlPaso.business.data.entity.Agencia;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;
import spring.estiloAlPaso.business.data.entity.Cliente.EstadoCliente;
import spring.estiloAlPaso.business.data.entity.Paquete.EstadoPaquete;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;
import spring.estiloAlPaso.business.data.repository.AgenciaRepository;
import spring.estiloAlPaso.business.data.repository.ClienteRepository;
import spring.estiloAlPaso.business.data.repository.PaqueteRepository;
import spring.estiloAlPaso.business.domain.mapper.ClienteMapper;
import spring.estiloAlPaso.business.domain.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.estiloAlPaso.business.domain.util.ClienteEstadoCalculator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final AgenciaRepository agenciaRepository;
    private final PaqueteRepository paqueteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public Page<ClienteListaResponseDto> listarClientes(
            String usuarioTikTok,
            String telefono,
            Pageable pageable
    ) {

        return clienteRepository
                .filtrarClientes(usuarioTikTok, telefono, pageable)
                .map(cliente -> {

                    BigDecimal total = cliente.getPaquetes().stream()
                            .flatMap(p -> p.getPrendas().stream())
                            .map(Prenda::getPrecioTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal pagado = cliente.getPaquetes().stream()
                            .flatMap(p -> p.getPrendas().stream())
                            .map(Prenda::getPrecioPagado)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    EstadoClienteLista estado =
                            ClienteEstadoCalculator.calcularEstado(total, pagado);

                    ClienteListaResponseDto dto = clienteMapper.toListaDto(cliente);

                    return new ClienteListaResponseDto(
                            dto.id(),
                            dto.usuarioTikTok(),
                            dto.telefono(),
                            dto.ciudad(),
                            dto.direccion(),
                            dto.agencia(),
                            estado
                    );
                });
    }

    @Override
    public ClienteDetalleResponseDto obtenerPorId(Integer id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (cliente.getEstado() != EstadoCliente.ACTIVO) {
            throw new NotFoundException("Cliente no disponible");
        }

        Paquete paquete = paqueteRepository
                .findByClienteIdAndEstado(cliente.getId(), EstadoPaquete.ACTIVO)
                .orElse(null);

        ClienteDetalleResponseDto dto = clienteMapper.toDetalleDto(cliente);

        PaqueteDetalleDto paqueteDto = null;

        if (paquete != null) {

            List<PrendaResponseDto> prendas = paquete.getPrendas()
                    .stream()
                    .map(p -> new PrendaResponseDto(
                            p.getId(),
                            p.getDescripcion(),
                            p.getPrecioPagado(),
                            p.getPrecioTotal(),
                            p.getEstado()
                    ))
                    .toList();

            BigDecimal total = paquete.getPrendas().stream()
                    .map(Prenda::getPrecioTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal pagado = paquete.getPrendas().stream()
                    .map(Prenda::getPrecioPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            paqueteDto = new PaqueteDetalleDto(
                    prendas.size(),
                    total,
                    pagado,
                    total.subtract(pagado),
                    prendas
            );
        }

        return ClienteDetalleResponseDto.builder()
                .id(dto.id())
                .nombreReal(dto.nombreReal())
                .usuarioTikTok(dto.usuarioTikTok())
                .dni(dto.dni())
                .telefono(dto.telefono())
                .direccion(dto.direccion())
                .agencia(dto.agencia())
                .ciudad(dto.ciudad())
                .paquete(paqueteDto)
                .build();
    }

    @Override
    @Transactional
    public ClienteListaResponseDto crearCliente(ClienteCreateRequestDto request) {

        String usuarioTikTok = request.usuarioTikTok().trim();
        String telefono = request.telefono().trim();
        String dni = request.dni() != null ? request.dni().trim() : null;

        if (dni != null && dni.isBlank()) {
            dni = null;
        }

        if (clienteRepository.existsByUsuarioTikTok(usuarioTikTok)) {
            throw new BusinessException("El nombreUser TikTok ya existe");
        }

        if (clienteRepository.existsByTelefono(telefono)) {
            throw new BusinessException("El teléfono ya está registrado");
        }

        if (dni != null && clienteRepository.existsByDni(dni)) {
            throw new BusinessException("El DNI ya está registrado");
        }

        Cliente cliente = clienteMapper.toEntity(request);

        cliente.setUsuarioTikTok(usuarioTikTok);
        cliente.setTelefono(telefono);
        cliente.setDni(dni);
        cliente.setFechaCreacion(new Date());
        cliente.setEstado(EstadoCliente.ACTIVO);

        if (request.ciudad() != null) {

            boolean esTrujillo = request.ciudad().equalsIgnoreCase("Trujillo");

            if (esTrujillo) {
                cliente.setDireccion(request.direccion());
                cliente.setAgencia(null);
            } else {
                if (request.agenciaId() != null) {
                    Agencia agencia = agenciaRepository.findById(request.agenciaId())
                            .orElseThrow(() -> new BusinessException("Agencia no válida"));
                    cliente.setAgencia(agencia);
                }
                cliente.setDireccion(null);
            }
        }

        Cliente guardado = clienteRepository.save(cliente);

        ClienteListaResponseDto dto = clienteMapper.toListaDto(guardado);

        return new ClienteListaResponseDto(
                dto.id(),
                dto.usuarioTikTok(),
                dto.telefono(),
                dto.ciudad(),
                dto.direccion(),
                dto.agencia(),
                EstadoClienteLista.SIN_PRENDAS
        );
    }

    @Override
    @Transactional
    public ClienteDetalleResponseDto actualizarCliente(Integer id, ClienteUpdateRequestDto request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (cliente.getEstado() != EstadoCliente.ACTIVO) {
            throw new NotFoundException("Cliente no disponible");
        }

        if (request.usuarioTikTok() != null) {
            String nuevo = request.usuarioTikTok().trim();

            if (clienteRepository.existsByUsuarioTikTokAndIdNot(nuevo, id)) {
                throw new BusinessException("El nombreUser TikTok ya existe");
            }

            cliente.setUsuarioTikTok(nuevo);
        }

        if (request.telefono() != null) {
            String nuevo = request.telefono().trim();

            if (clienteRepository.existsByTelefonoAndIdNot(nuevo, id)) {
                throw new BusinessException("El teléfono ya está registrado");
            }

            cliente.setTelefono(nuevo);
        }

        if (request.dni() != null) {
            String nuevo = request.dni().trim();

            if (nuevo.isBlank()) {
                cliente.setDni(null);
            } else {
                if (clienteRepository.existsByDniAndIdNot(nuevo, id)) {
                    throw new BusinessException("El DNI ya está registrado");
                }
                cliente.setDni(nuevo);
            }
        }

        clienteMapper.updateEntity(request, cliente);

        String ciudad = cliente.getCiudad();
        boolean esTrujillo = ciudad != null && ciudad.equalsIgnoreCase("Trujillo");

        if (esTrujillo) {
            if (request.direccion() != null) {
                cliente.setDireccion(request.direccion().trim());
            }
            cliente.setAgencia(null);
        } else {
            if (request.agenciaId() != null) {

                if (request.agenciaId() == 0) {
                    cliente.setAgencia(null);
                } else {
                    Agencia agencia = agenciaRepository.findById(request.agenciaId())
                            .orElseThrow(() -> new BusinessException("Agencia no válida"));

                    cliente.setAgencia(agencia);
                }
            }
            cliente.setDireccion(null);
        }

        Cliente actualizado = clienteRepository.save(cliente);

        return clienteMapper.toDetalleDto(actualizado);
    }

    @Override
    @Transactional
    public void eliminarCliente(Integer id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        if (cliente.getEstado() != EstadoCliente.ACTIVO) {
            throw new BusinessException("El cliente ya está inactivo");
        }

        cliente.setEstado(EstadoCliente.INACTIVO);
        clienteRepository.save(cliente);
    }
}
