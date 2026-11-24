package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Transacao;
import com.bankflow.joao.dto.request.TransacaoRequest;
import com.bankflow.joao.dto.response.TransacaoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransacaoMapper {

    public Transacao toEntity(TransacaoRequest req, Conta destino, Conta origem) {
        Transacao transacao = new Transacao();
        transacao.setChaveDestino(req.chaveDestino());
        transacao.setDestino(destino);
        transacao.setIdempotencykey(req.idempotencyKey());
        transacao.setOrigem(origem);
        transacao.setStatus(req.status());
        transacao.setTipo(req.tipo());
        transacao.setValor(req.valor());
        return transacao;
    }

    public TransacaoResponse toDTO(Transacao transacao) {
        return new TransacaoResponse(
                transacao.getOrigem().getId(), transacao.getDestino().getId(), transacao.getTipo(), transacao.getValor(),
                transacao.getStatus(), transacao.getChaveDestino(), transacao.getIdempotencykey(), transacao.getCriadaEm()
        );
    }

}
