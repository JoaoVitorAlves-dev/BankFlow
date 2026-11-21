package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.TipoLancamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record LancamentoRequest(
        @NotNull Long transacaoId,
        @NotNull Long contaId,
        @NotNull TipoLancamento tipo,
        @NotNull @PositiveOrZero BigDecimal valor,
        @NotNull @PositiveOrZero BigDecimal saldoapos
) {
}
