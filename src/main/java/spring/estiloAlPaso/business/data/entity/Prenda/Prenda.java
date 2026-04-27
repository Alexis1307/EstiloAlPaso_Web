package spring.estiloAlPaso.business.data.entity.Prenda;

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
@Table(name = "prendas")
public class Prenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precioTotal;

    @Column(nullable = false)
    private BigDecimal precioPagado;

    @Enumerated(EnumType.STRING)
    private EstadoPrenda estado;
    private Date fechaVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paqueteId", nullable = false)
    private Paquete paquete;
}
