package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.data.entity.Paquete.EstadoPaquete;
import spring.estiloAlPaso.business.data.entity.Paquete.Paquete;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaqueteRepository extends JpaRepository<Paquete, Integer> {
    Optional<Paquete> findByClienteIdAndEstado(Integer clienteId, EstadoPaquete estado);
    List<Paquete> findByClienteIdOrderByEstadoDescFechaCreacionDesc(Integer clienteId);

}
