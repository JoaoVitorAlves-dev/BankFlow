package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.Bandeira;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CartaoRequest(
        @NotNull Long contaId,
        @NotBlank String ultimos4,
        @NotNull Bandeira bandeira,
        @NotNull YearMonth validade,
        @NotNull @PositiveOrZero BigDecimal limite
) {
}
