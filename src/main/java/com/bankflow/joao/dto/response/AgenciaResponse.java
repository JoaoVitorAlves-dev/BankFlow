package com.bankflow.joao.dto.response;

public record AgenciaResponse(
        String codigo,
        String nome,
        String cidade,
        String estado
) {
}
