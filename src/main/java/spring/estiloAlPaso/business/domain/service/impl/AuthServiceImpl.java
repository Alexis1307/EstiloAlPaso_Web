package spring.estiloAlPaso.business.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.estiloAlPaso.business.api.dto.login.LoginRequestDto;
import spring.estiloAlPaso.business.api.dto.login.LoginResponseDto;
import spring.estiloAlPaso.business.data.entity.Usuario;
import spring.estiloAlPaso.business.data.repository.UsuarioRepository;
import spring.estiloAlPaso.business.domain.service.AuthService;
import spring.estiloAlPaso.security.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto request) {


        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.usuario(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new LoginResponseDto(
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority(),
                token
        );
    }
}
