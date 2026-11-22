package com.bankflow.joao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GerenteRequest(
        @NotNull Long usuarioId,
        @NotNull Long agenciaId,
        @NotBlank String matricula
) {
}
