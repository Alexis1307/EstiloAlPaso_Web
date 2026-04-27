package spring.estiloAlPaso.business.api.dto.usuario;

public record UsuarioRequestDto(
        String username,
        String password,
        String rol
) {}
