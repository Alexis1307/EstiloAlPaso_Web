package spring.estiloAlPaso.business.api.dto.login;

public record LoginResponseDto(
        String usuario,
        String rol,
        String token
) {}
