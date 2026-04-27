package spring.estiloAlPaso.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.data.entity.Rol;
import spring.estiloAlPaso.business.data.entity.Usuario;
import spring.estiloAlPaso.business.data.repository.UsuarioRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByNombreUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getNombreUser(),
                usuario.getContraHash(),
                Collections.singletonList(mapRolToAuthority(usuario.getRol()))
        );
    }

    private GrantedAuthority mapRolToAuthority(Rol rol) {
        return new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol());
    }
}
