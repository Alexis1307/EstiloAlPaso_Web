package spring.estiloAlPaso.business.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.usuario.UsuarioRequestDto;
import spring.estiloAlPaso.business.data.entity.Rol;
import spring.estiloAlPaso.business.data.entity.Usuario;
import spring.estiloAlPaso.business.data.repository.RolRepository;
import spring.estiloAlPaso.business.data.repository.UsuarioRepository;
import spring.estiloAlPaso.business.domain.service.UsuarioService;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario crear(UsuarioRequestDto dto) {

        Rol rol = rolRepository.findByNombreRol(dto.rol())
                .orElseThrow();

        Usuario usuario = new Usuario();
        usuario.setNombreUser(dto.username());
        usuario.setContraHash(passwordEncoder.encode(dto.password()));
        usuario.setFechaCreacion(new Date());
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
