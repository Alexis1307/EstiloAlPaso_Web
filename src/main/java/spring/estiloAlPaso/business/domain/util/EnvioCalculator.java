package spring.estiloAlPaso.business.domain.util;

import spring.estiloAlPaso.business.api.dto.cliente.EstadoClienteLista;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;

import java.math.BigDecimal;
import java.util.Objects;

public final class EnvioCalculator {

    private EnvioCalculator() {}

    public static BigDecimal calcularTotal(Paquete paquete) {
        if (paquete == null || paquete.getPrendas() == null) return BigDecimal.ZERO;

        return paquete.getPrendas().stream()
                .map(Prenda::getPrecioTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calcularPagado(Paquete paquete) {
        if (paquete == null || paquete.getPrendas() == null) return BigDecimal.ZERO;

        return paquete.getPrendas().stream()
                .map(Prenda::getPrecioPagado)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static EstadoClienteLista calcularEstadoCliente(Paquete paquete) {

        BigDecimal total = calcularTotal(paquete);
        BigDecimal pagado = calcularPagado(paquete);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return EstadoClienteLista.SIN_PRENDAS;
        }

        return pagado.compareTo(total) >= 0
                ? EstadoClienteLista.PAGADO
                : EstadoClienteLista.PENDIENTE;
    }
}
