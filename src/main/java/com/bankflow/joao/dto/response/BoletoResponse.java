package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusBoleto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BoletoResponse(
        Long contaId,
        String codigoBarras,
        BigDecimal valor,
        LocalDate vencimento,
        StatusBoleto status
) {
}
