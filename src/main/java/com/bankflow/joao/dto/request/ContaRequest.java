package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ContaRequest(
        @NotNull Long usuarioId,
        @NotNull Long agenciaId,
        @NotBlank String numero,
        @NotNull TipoConta tipo,
        @NotNull @PositiveOrZero BigDecimal saldo,
        @NotNull @PositiveOrZero BigDecimal limite,
        @NotNull StatusConta status,
        @NotNull Long version
) {
}
