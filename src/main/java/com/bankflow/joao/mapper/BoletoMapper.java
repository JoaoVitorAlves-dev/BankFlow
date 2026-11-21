package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Boleto;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.dto.request.BoletoRequest;
import com.bankflow.joao.dto.response.BoletoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BoletoMapper {

    public Boleto toEntity(BoletoRequest req, Conta conta) {
        Boleto boleto = new Boleto();
        boleto.setCodigoBarras(req.codigoBarras());
        boleto.setConta(conta);
        boleto.setValor(req.valor());
        boleto.setVencimento(req.vencimento());
        return boleto;
    }

    public BoletoResponse toDTO(Boleto boleto) {
        return new BoletoResponse(
                boleto.getConta().getId(), boleto.getCodigoBarras(), boleto.getValor(), boleto.getVencimento(), boleto.getStatus());
    }

}
