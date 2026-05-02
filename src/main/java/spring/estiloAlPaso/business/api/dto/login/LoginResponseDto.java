package spring.estiloAlPaso.business.api.dto.login;

public record LoginResponseDto(
        String nombreUser,
        String rol,
        String token
) {}
