package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Lancamento;
import com.bankflow.joao.domain.Transacao;
import com.bankflow.joao.dto.request.LancamentoRequest;
import com.bankflow.joao.dto.response.LancamentoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LancamentoMapper {

    public Lancamento toEntity(LancamentoRequest req, Conta conta, Transacao transacao) {
        Lancamento lancamento = new Lancamento();
        lancamento.setConta(conta);
        lancamento.setSaldoApos(req.saldoapos());
        lancamento.setTipo(req.tipo());
        lancamento.setTransacao(transacao);
        lancamento.setValor(req.valor());
        return lancamento;
    }

    public LancamentoResponse toDTO(Lancamento lancamento) {
        return new LancamentoResponse(
                lancamento.getTransacao().getId(), lancamento.getConta().getId(), lancamento.getTipo(), lancamento.getValor(), lancamento.getSaldoApos()
        );
    }

}
