package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Emprestimo;
import com.bankflow.joao.dto.request.EmprestimoRequest;
import com.bankflow.joao.dto.response.EmprestimoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmprestimoMapper {

    public Emprestimo toEntity(EmprestimoRequest req, Conta conta) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setConta(conta);
        emprestimo.setQtdParcelas(req.qtdParcelas());
        emprestimo.setTaxa_juros(req.taxaJuros());
        emprestimo.setValor(req.valor());
        return emprestimo;
    }

    public EmprestimoResponse toDTO(Emprestimo emprestimo) {
        return new EmprestimoResponse(
                emprestimo.getConta().getId(), emprestimo.getValor(), emprestimo.getTaxa_juros(), emprestimo.getQtdParcelas(),
                emprestimo.getStatusEmprestimo()
        );
    }

}
