package com.bankflow.joao.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record FaturaRequest(
        @NotNull Long cartaoId,
        @NotNull YearMonth mesRef,
        @NotNull @PositiveOrZero BigDecimal valorTotal,
        @NotNull LocalDate vencimento
) {
}
