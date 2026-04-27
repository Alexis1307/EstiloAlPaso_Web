package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.data.entity.Cliente.Cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query("""
        SELECT c
        FROM Cliente c
        WHERE c.estado = 'ACTIVO'
          AND (:usuarioTikTok IS NULL OR LOWER(c.usuarioTikTok) LIKE LOWER(CONCAT('%', :usuarioTikTok, '%')))
          AND (:telefono IS NULL OR c.telefono LIKE CONCAT('%', :telefono, '%'))
    """)
    Page<Cliente> filtrarClientes(
            @Param("usuarioTikTok") String usuarioTikTok,
            @Param("telefono") String telefono,
            Pageable pageable
    );

    boolean existsByUsuarioTikTok(String usuarioTikTok);

    boolean existsByTelefono(String telefono);

    boolean existsByDni(String dni);

    boolean existsByUsuarioTikTokAndIdNot(String usuarioTikTok, Integer id);

    boolean existsByTelefonoAndIdNot(String telefono, Integer id);

    boolean existsByDniAndIdNot(String dni, Integer id);
}
