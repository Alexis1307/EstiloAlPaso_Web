package spring.estiloAlPaso.business.data.entity.Envio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "envios")
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paqueteId", nullable = false)
    private Paquete paquete;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoenvio")
    private TipoEnvio tipoEnvio;

    private Integer cantidadPrendas;
    private BigDecimal total;
    private BigDecimal pagado;

    private String clave;
    private Date fechaCreacion;
    private Date fechaEnvio;
}

