package com.bankflow.joao.dto.response;

public record GerenteResponse(
        Long id,
        Long usuarioId,
        Long agenciaId,
        String matricula,
        Boolean ativo
) {
}
