package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusTransacao;
import com.bankflow.joao.domain.roles.TipoTransacao;

import java.math.BigDecimal;
import java.time.Instant;

public record TransacaoResponse(
        Long contaOrigemId,
        Long contaDestinoId,
        TipoTransacao tipo,
        BigDecimal valor,
        StatusTransacao status,
        String chaveDestino,
        String idempotencyKey,
        Instant criadaEm
) {
}
