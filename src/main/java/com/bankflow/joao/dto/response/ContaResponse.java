package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.TipoConta;

import java.math.BigDecimal;
import java.time.Instant;

public record ContaResponse(
        Long usuarioId,
        Long agenciaId,
        String numero,
        TipoConta tipo,
        BigDecimal saldo,
        BigDecimal limite,
        StatusConta status,
        Long version,
        Instant criadaEm
) {
}
