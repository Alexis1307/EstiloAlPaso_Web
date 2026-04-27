package spring.estiloAlPaso.business.domain.util;

import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;

import java.math.BigDecimal;
import java.util.Objects;

public final class PaqueteCalculator {

    private PaqueteCalculator() {}

    public static BigDecimal calcularTotal(Paquete paquete) {

        if (paquete.getPrendas() == null) return BigDecimal.ZERO;

        return paquete.getPrendas().stream()
                .map(Prenda::getPrecioTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calcularPagado(Paquete paquete) {

        if (paquete.getPrendas() == null) return BigDecimal.ZERO;

        return paquete.getPrendas().stream()
                .map(Prenda::getPrecioPagado)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calcularPendiente(Paquete paquete) {

        return calcularTotal(paquete).subtract(calcularPagado(paquete));
    }
}
