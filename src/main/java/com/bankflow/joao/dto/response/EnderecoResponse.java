package com.bankflow.joao.dto.response;

public record EnderecoResponse(
        String rua,
        String cidade,
        String estado,
        String cep
) {
}
