package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.data.entity.Envio.Envio;
import spring.estiloAlPaso.business.data.entity.Envio.EstadoEnvio;
import spring.estiloAlPaso.business.data.entity.Envio.TipoEnvio;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {

    boolean existsByPaqueteIdAndEstado(Integer paqueteId, EstadoEnvio estado);

    List<Envio> findAllByPaqueteClienteId(Integer clienteId);

    List<Envio> findByTipoEnvioAndEstado(TipoEnvio tipoEnvio, EstadoEnvio estado);
    boolean existsByClave(String clave);

    Optional<Envio> findByPaqueteClienteIdAndEstado(Integer clienteId, EstadoEnvio estado);

    List<Envio> findAllByPaqueteClienteIdAndEstado(Integer clienteId, EstadoEnvio estado);}
