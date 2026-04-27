package spring.estiloAlPaso.business.domain.service;

import spring.estiloAlPaso.business.api.dto.login.LoginRequestDto;
import spring.estiloAlPaso.business.api.dto.login.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto request);
}
