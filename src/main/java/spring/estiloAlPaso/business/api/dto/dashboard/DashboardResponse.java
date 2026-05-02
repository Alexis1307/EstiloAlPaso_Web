package spring.estiloAlPaso.business.api.dto.dashboard;

import java.util.List;

public record DashboardResponse(
        DashboardCardsDto cards,
        List<PaqueteActivoDto> paquetesActivos,
        List<ClienteDeudaDto> clientesDeuda,
        List<ClienteMantenimientoDto> mantenimiento
) {}
