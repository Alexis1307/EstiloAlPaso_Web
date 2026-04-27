package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.data.entity.Prenda.Prenda;

@Repository
public interface PrendaRepository extends JpaRepository<Prenda, Integer> {
}
