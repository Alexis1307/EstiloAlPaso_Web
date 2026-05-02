package spring.estiloAlPaso.business.domain.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.dashboard.*;
import spring.estiloAlPaso.business.api.projection.DashboardCardsProjection;
import spring.estiloAlPaso.business.data.repository.DashboardRepository;
import spring.estiloAlPaso.business.domain.service.DashboardService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository repository;


    @Override
    public DashboardResponse obtenerDashboard() {

        DashboardCardsProjection cardsProj = repository.getDashboardCards();

        DashboardCardsDto cards = new DashboardCardsDto(
                cardsProj.getPaquetesActivos(),
                cardsProj.getEnviosPendientes(),
                cardsProj.getMontoTotal(),
                cardsProj.getMontoPagado(),
                cardsProj.getMontoPendiente()
        );

        List<PaqueteActivoDto> paquetes = repository.getPaquetesActivosTop10()
                .stream()
                .map(p -> new PaqueteActivoDto(
                        p.getUsuario(),
                        p.getCantidadPrendas(),
                        p.getEstadoPago()
                ))
                .toList();

        List<ClienteDeudaDto> deuda = repository.getClientesDeudaTop10()
                .stream()
                .map(d -> new ClienteDeudaDto(
                        d.getUsuario(),
                        d.getDeudaTotal()
                ))
                .toList();

        List<ClienteMantenimientoDto> mantenimiento = repository.getMantenimientoTop10()
                .stream()
                .map(m -> new ClienteMantenimientoDto(
                        m.getUsuario(),
                        m.getPrendasMantenimiento()
                ))
                .toList();

        return new DashboardResponse(
                cards,
                paquetes,
                deuda,
                mantenimiento
        );
    }
}
