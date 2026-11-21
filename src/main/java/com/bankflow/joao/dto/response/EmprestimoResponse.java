package com.bankflow.joao.dto.response;

import com.bankflow.joao.domain.roles.StatusEmprestimo;

import java.math.BigDecimal;

public record EmprestimoResponse(
        Long contaId,
        BigDecimal valor,
        BigDecimal taxaJuros,
        Integer qtdParcelas,
        StatusEmprestimo status
) {
}
