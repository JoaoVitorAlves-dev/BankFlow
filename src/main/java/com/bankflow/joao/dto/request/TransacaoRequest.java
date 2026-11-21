package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.StatusTransacao;
import com.bankflow.joao.domain.roles.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record TransacaoRequest(
        @NotNull Long contaOrigemId,
        @NotNull Long contaDestinoId,
        @NotNull TipoTransacao tipo,
        @NotNull @PositiveOrZero BigDecimal valor,
        @NotNull StatusTransacao status,
        @NotBlank String chaveDestino,
        @NotBlank String idempotencyKey
) {
}
