package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.TipoInvestimento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestimentoRequest(
        @NotNull Long contaId,
        @NotNull TipoInvestimento tipo,
        @NotNull @PositiveOrZero BigDecimal valorAplicado,
        @NotNull @PositiveOrZero BigDecimal taxaAnual,
        @NotNull LocalDate vencimento
) {
}
