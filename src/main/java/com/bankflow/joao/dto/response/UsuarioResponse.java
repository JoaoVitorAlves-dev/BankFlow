package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusUsuario;

public record UsuarioResponse(
        String nome,
        String cpf,
        String email,
        StatusUsuario status
) {
}
