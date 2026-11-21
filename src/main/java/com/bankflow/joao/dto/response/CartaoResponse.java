package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.Bandeira;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CartaoResponse(
        Long contaId,
        String ultimos4,
        Bandeira bandeira,
        YearMonth validade,
        BigDecimal limite,
        Boolean ativo
) {
}
