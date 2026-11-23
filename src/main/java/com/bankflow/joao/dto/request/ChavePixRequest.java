package com.bankflow.joao.dto.request;

import com.bankflow.joao.domain.roles.TipoChave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChavePixRequest(
        @NotNull Long contaId,
        @NotNull TipoChave tipo,
        @NotBlank String valor
) {
}
