package com.bankflow.joao.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record EmprestimoRequest(
        @NotNull Long contaId,
        @NotNull @PositiveOrZero BigDecimal valor,
        @NotNull @PositiveOrZero BigDecimal taxaJuros,
        @NotNull Integer qtdParcelas
) {
}
