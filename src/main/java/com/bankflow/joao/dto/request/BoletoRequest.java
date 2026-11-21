package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.StatusBoleto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BoletoRequest(
        @NotNull Long contaId,
        @NotBlank String codigoBarras,
        @NotNull @PositiveOrZero BigDecimal valor,
        @NotNull LocalDate vencimento
) {
}
