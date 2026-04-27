package spring.estiloAlPaso.business.domain.util;

import org.springframework.stereotype.Component;
import spring.estiloAlPaso.business.api.dto.cliente.EstadoClienteLista;


import java.math.BigDecimal;

@Component
public class ClienteEstadoCalculator {

    private ClienteEstadoCalculator() {}

    public static EstadoClienteLista calcularEstado(BigDecimal total, BigDecimal pagado) {

        if (total == null) total = BigDecimal.ZERO;
        if (pagado == null) pagado = BigDecimal.ZERO;

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return EstadoClienteLista.SIN_PRENDAS;
        }

        return pagado.compareTo(total) >= 0
                ? EstadoClienteLista.PAGADO
                : EstadoClienteLista.PENDIENTE;
    }
}
