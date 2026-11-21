package com.bankflow.joao.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AgenciaRequest(
        @NotBlank String codigo,
        @NotBlank String nome,
        @NotBlank String cidade,
        @NotBlank String estado
) {
}
