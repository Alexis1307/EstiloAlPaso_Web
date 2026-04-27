package spring.estiloAlPaso.business.domain.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.prenda.*;
import spring.estiloAlPaso.business.api.exeptions.BusinessException;
import spring.estiloAlPaso.business.api.exeptions.NotFoundException;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;
import spring.estiloAlPaso.business.data.entity.Paquete.EstadoPaquete;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;
import spring.estiloAlPaso.business.data.repository.ClienteRepository;
import spring.estiloAlPaso.business.data.repository.PaqueteRepository;
import spring.estiloAlPaso.business.data.repository.PrendaRepository;
import spring.estiloAlPaso.business.domain.mapper.PaqueteMapper;
import spring.estiloAlPaso.business.domain.mapper.PrendaMapper;
import spring.estiloAlPaso.business.domain.service.PaqueteService;
import spring.estiloAlPaso.business.domain.service.PrendaService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrendaServiceImpl implements PrendaService {

    private final ClienteRepository clienteRepository;
    private final PaqueteRepository paqueteRepository;
    private final PrendaRepository prendaRepository;
    private final PaqueteService paqueteService;
    private final PaqueteMapper paqueteMapper;
    private final PrendaMapper prendaMapper;

    @Transactional
    public RegistrarPrendasResponseDto registrarPrendas(RegistrarPrendasRequestDto request) {

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

        Paquete paquete = paqueteService.obtenerOCrearPaqueteActivo(cliente);

        List<Prenda> prendas = request.prendas().stream()
                .map(item -> {

                    validarPrenda(item);

                    Prenda prenda = new Prenda();
                    prenda.setDescripcion(item.descripcion());
                    prenda.setPrecioTotal(item.precioTotal());
                    prenda.setPrecioPagado(item.precioPagado());
                    prenda.setEstado(item.estado());
                    prenda.setFechaVenta(new Date());
                    prenda.setPaquete(paquete);

                    return prenda;
                })
                .toList();

        paquete.getPrendas().addAll(prendas);
        paqueteRepository.save(paquete);

        return paqueteMapper.toRegistrarResponse(paquete);
    }

    private void validarPrenda(PrendaItemRequestDto item) {

        if (item.precioTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio total debe ser mayor a 0");
        }

        if (item.precioPagado().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("El precio pagado no puede ser negativo");
        }

        if (item.precioPagado().compareTo(item.precioTotal()) > 0) {
            throw new BusinessException("El precio pagado no puede ser mayor al total");
        }
    }

    private RegistrarPrendasResponseDto construirRespuesta(Paquete paquete) {

        BigDecimal total = BigDecimal.ZERO;
        BigDecimal pagado = BigDecimal.ZERO;

        for (Prenda p : paquete.getPrendas()) {
            total = total.add(p.getPrecioTotal());
            pagado = pagado.add(p.getPrecioPagado());
        }

        return new RegistrarPrendasResponseDto(
                paquete.getCliente().getId(),
                paquete.getCliente().getUsuarioTikTok(),
                paquete.getPrendas().size(),
                total,
                pagado,
                total.subtract(pagado),
                paquete.getId()
        );
    }

    @Transactional
    public RegistrarPrendasResponseDto actualizarPrenda(Integer prendaId, ActualizarPrendaRequestDto request) {

        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new NotFoundException("Prenda no encontrada"));

        Paquete paquete = prenda.getPaquete();

        BigDecimal nuevoTotal = request.precioTotal() != null ? request.precioTotal() : prenda.getPrecioTotal();
        BigDecimal nuevoPagado = request.precioPagado() != null ? request.precioPagado() : prenda.getPrecioPagado();

        if (nuevoPagado.compareTo(nuevoTotal) > 0) {
            throw new BusinessException("El precio pagado no puede ser mayor al total");
        }

        if (request.descripcion() != null) {
            prenda.setDescripcion(request.descripcion());
        }

        prenda.setPrecioTotal(nuevoTotal);
        prenda.setPrecioPagado(nuevoPagado);

        if (request.estado() != null) {
            prenda.setEstado(request.estado());
        }

        prendaRepository.save(prenda);

        return paqueteMapper.toRegistrarResponse(paquete);
    }

    @Transactional
    public RegistrarPrendasResponseDto eliminarPrenda(Integer prendaId) {

        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new NotFoundException("Prenda no encontrada"));

        Paquete paquete = prenda.getPaquete();

        paquete.getPrendas().remove(prenda);
        prendaRepository.delete(prenda);

        return paqueteMapper.toRegistrarResponse(paquete);
    }

    public PrendaResponseDto obtenerPrenda(Integer prendaId) {

        Prenda prenda = prendaRepository.findById(prendaId)
                .orElseThrow(() -> new NotFoundException("Prenda no encontrada"));

        return prendaMapper.toResponseDto(prenda);
    }
}
