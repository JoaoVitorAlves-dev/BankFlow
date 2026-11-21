package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.TipoLancamento;

import java.math.BigDecimal;

public record LancamentoResponse(
        Long transacaoId,
        Long contaId,
        TipoLancamento tipo,
        BigDecimal valor,
        BigDecimal saldoApos
) {
}
