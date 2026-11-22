package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Emprestimo;
import com.bankflow.joao.domain.Parcela;
import com.bankflow.joao.dto.request.ParcelaRequest;
import com.bankflow.joao.dto.response.ParcelaResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParcelaMapper {

    public Parcela toEntity(ParcelaRequest req, Emprestimo emprestimo) {
        Parcela parcela = new Parcela();
        parcela.setEmprestimo(emprestimo);
        parcela.setNumero(req.numero());
        parcela.setValor(req.valor());
        parcela.setVencimento(req.vencimento());
        return parcela;
    }

    public ParcelaResponse toDTO(Parcela parcela) {
        return new ParcelaResponse(
                parcela.getEmprestimo().getId(), parcela.getNumero(), parcela.getValor(), parcela.getVencimento(), parcela.getStatus()
        );
    }

}
