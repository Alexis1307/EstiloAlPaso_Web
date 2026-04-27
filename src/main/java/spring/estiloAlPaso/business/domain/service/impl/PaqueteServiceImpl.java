package spring.estiloAlPaso.business.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteDetalleDto;
import spring.estiloAlPaso.business.api.dto.paquete.PaqueteResumenDto;
import spring.estiloAlPaso.business.api.dto.prenda.PrendaResponseDto;
import spring.estiloAlPaso.business.api.exeptions.NotFoundException;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;
import spring.estiloAlPaso.business.data.entity.Cliente.EstadoCliente;
import spring.estiloAlPaso.business.data.entity.Paquete.EstadoPaquete;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;
import spring.estiloAlPaso.business.data.repository.ClienteRepository;
import spring.estiloAlPaso.business.data.repository.PaqueteRepository;
import spring.estiloAlPaso.business.domain.service.PaqueteService;
import spring.estiloAlPaso.business.domain.util.PaqueteCalculator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaqueteServiceImpl implements PaqueteService {

    private final PaqueteRepository paqueteRepository;

    @Transactional
    public Paquete obtenerOCrearPaqueteActivo(Cliente cliente) {

        return paqueteRepository
                .findByClienteIdAndEstado(cliente.getId(), EstadoPaquete.ACTIVO)
                .orElseGet(() -> {

                    Paquete nuevo = new Paquete();
                    nuevo.setCliente(cliente);
                    nuevo.setEstado(EstadoPaquete.ACTIVO);
                    nuevo.setFechaCreacion(new Date());

                    return paqueteRepository.save(nuevo);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaqueteResumenDto> listarPorCliente(Integer clienteId) {

        List<Paquete> paquetes = paqueteRepository
                .findByClienteIdOrderByEstadoDescFechaCreacionDesc(clienteId);

        return paquetes.stream()
                .sorted((p1, p2) -> {
                    if (p1.getEstado() == EstadoPaquete.ACTIVO) return -1;
                    if (p2.getEstado() == EstadoPaquete.ACTIVO) return 1;
                    return p2.getFechaCreacion().compareTo(p1.getFechaCreacion());
                })
                .map(paquete -> {

                    BigDecimal total = PaqueteCalculator.calcularTotal(paquete);
                    BigDecimal pagado = PaqueteCalculator.calcularPagado(paquete);
                    BigDecimal pendiente = PaqueteCalculator.calcularPendiente(paquete);

                    return new PaqueteResumenDto(
                            paquete.getId(),
                            paquete.getEstado(),
                            paquete.getPrendas().size(),
                            total,
                            pagado,
                            pendiente
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaqueteDetalleDto obtenerDetalle(Integer paqueteId) {

        Paquete paquete = paqueteRepository.findById(paqueteId)
                .orElseThrow(() -> new NotFoundException("Paquete no encontrado"));

        List<PrendaResponseDto> prendas = paquete.getPrendas()
                .stream()
                .map(p -> new PrendaResponseDto(
                        p.getDescripcion(),
                        p.getPrecioPagado(),
                        p.getPrecioTotal(),
                        p.getEstado()
                ))
                .toList();

        BigDecimal total = PaqueteCalculator.calcularTotal(paquete);
        BigDecimal pagado = PaqueteCalculator.calcularPagado(paquete);
        BigDecimal pendiente = PaqueteCalculator.calcularPendiente(paquete);

        return new PaqueteDetalleDto(
                prendas.size(),
                total,
                pagado,
                pendiente,
                prendas
        );
    }

}
