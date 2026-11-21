package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusFatura;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record FaturaResponse(
        Long cartaoId,
        YearMonth mesRef,
        BigDecimal valorTotal,
        LocalDate vencimento,
        StatusFatura status
) {
}
