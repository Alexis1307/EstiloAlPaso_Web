package spring.estiloAlPaso.business.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.estiloAlPaso.business.data.entity.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.nombreUser = :username")
    Optional<Usuario> findByNombreUser(@Param("username") String username);;

    boolean existsByNombreUser(String nombreUser);

    boolean existsByNombreUserAndIdNot(String nombreUser, Integer id);

}


