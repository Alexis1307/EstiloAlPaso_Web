package spring.estiloAlPaso.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import spring.estiloAlPaso.business.data.entity.Rol;
import spring.estiloAlPaso.business.data.entity.Usuario;
import spring.estiloAlPaso.business.data.repository.RolRepository;
import spring.estiloAlPaso.business.data.repository.UsuarioRepository;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (usuarioRepository.existsByNombreUser("AlxisJara")) return;

        Rol admin = rolRepository.findByNombreRol("ADMIN")
                .orElseThrow(() -> new RuntimeException("No existe rol ADMIN"));

        Usuario user = new Usuario();
        user.setNombreUser("AlxisJara");
        user.setContraHash(passwordEncoder.encode("Alexis1307"));
        user.setFechaCreacion(new Date());
        user.setRol(admin);

        usuarioRepository.save(user);
    }
}
