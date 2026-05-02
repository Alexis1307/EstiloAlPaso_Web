package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.data.entity.Agencia;

import java.util.Collection;
import java.util.List;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Integer> {
    @Query("""
        SELECT a
        FROM Agencia a
        WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
        ORDER BY a.nombre ASC
    """)
    Page<Agencia> buscarPorNombre(String search, Pageable pageable);

    boolean existsByNombreIgnoreCase(String nombre);

    List<Agencia> findTop10ByNombreContainingIgnoreCaseOrderByNombreAsc(String nombre);
}
