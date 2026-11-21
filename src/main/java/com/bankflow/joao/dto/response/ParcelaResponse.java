package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusParcela;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaResponse(
        Long emprestimoId,
        Integer numero,
        BigDecimal valor,
        LocalDate vencimento,
        StatusParcela status
) {
}
