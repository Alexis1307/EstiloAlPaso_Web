package spring.estiloAlPaso.business.domain.service;

import spring.estiloAlPaso.business.api.dto.usuario.UsuarioRequestDto;
import spring.estiloAlPaso.business.data.entity.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario crear(UsuarioRequestDto dto);
    List<Usuario> listar();
    void eliminar(Integer id);
}
