package spring.estiloAlPaso.business.data.entity.Cliente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.estiloAlPaso.business.data.entity.Agencia;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    private String nombreReal;

    @Column(nullable = false)
    private String usuarioTikTok;

    private String dni;

    @Column(nullable = false)
    private String telefono;

    private String direccion;

    @ManyToOne
    @JoinColumn(name = "agenciaId")
    private Agencia agencia;

    private String ciudad;
    private Date fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoCliente estado;

    @OneToMany(mappedBy = "cliente",fetch = FetchType.LAZY)
    private List<Paquete> paquetes = new ArrayList<>();
}

