package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Investimento;
import com.bankflow.joao.dto.request.InvestimentoRequest;
import com.bankflow.joao.dto.response.InvestimentoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InvestimentoMapper {

    public Investimento toEntity(InvestimentoRequest req, Conta conta) {
        Investimento investimento = new Investimento();
        investimento.setConta(conta);
        investimento.setTaxaAnual(req.taxaAnual());
        investimento.setTipo(req.tipo());
        investimento.setValorAplicado(req.valorAplicado());
        investimento.setVencimento(req.vencimento());
        return investimento;
    }

    public InvestimentoResponse toDTO(Investimento investimento) {
        return new InvestimentoResponse(
                investimento.getConta().getId(), investimento.getTipo(), investimento.getValorAplicado(), investimento.getTaxaAnual(), investimento.getVencimento()
        );
    }

}
