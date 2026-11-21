package com.bankflow.joao.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaRequest(
        @NotNull Long emprestimoId,
        @NotNull Integer numero,
        @NotNull @PositiveOrZero BigDecimal valor,
        @NotNull LocalDate vencimento
) {
}
