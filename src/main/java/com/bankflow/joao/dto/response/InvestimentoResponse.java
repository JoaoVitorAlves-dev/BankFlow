package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.TipoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestimentoResponse(
        Long contaId,
        TipoInvestimento tipo,
        BigDecimal valorAplicado,
        BigDecimal taxaAnual,
        LocalDate vencimento
) {
}
