package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.TipoChave;

import java.time.Instant;

public record ChavePixResponse(
        Long contaId,
        TipoChave tipo,
        String valor,
        Boolean ativa,
        Instant criadaEm
) {
}
