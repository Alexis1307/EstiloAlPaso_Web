package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.api.projection.ClienteDeudaProjection;
import spring.estiloAlPaso.business.api.projection.DashboardCardsProjection;
import spring.estiloAlPaso.business.api.projection.MantenimientoProjection;
import spring.estiloAlPaso.business.api.projection.PaqueteActivoProjection;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Prenda, Long> {

    @Query(value = "CALL sp_dashboard_cards()", nativeQuery = true)
    DashboardCardsProjection getDashboardCards();

    @Query(value = "CALL sp_paquetes_activos_top10()", nativeQuery = true)
    List<PaqueteActivoProjection> getPaquetesActivosTop10();

    @Query(value = "CALL sp_clientes_deuda_top10()", nativeQuery = true)
    List<ClienteDeudaProjection> getClientesDeudaTop10();

    @Query(value = "CALL sp_mantenimiento_top10()", nativeQuery = true)
    List<MantenimientoProjection> getMantenimientoTop10();
}
