package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.ChavePix;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.dto.request.ChavePixRequest;
import com.bankflow.joao.dto.response.ChavePixResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChavePixMapper {

    public ChavePix toEntity(ChavePixRequest req, Conta conta) {
        ChavePix chavePix = new ChavePix();
        chavePix.setConta(conta);
        chavePix.setTipo(req.tipo());
        chavePix.setValor(req.valor());
        return chavePix;
    }

    public ChavePixResponse toDTO(ChavePix chavePix) {
        return new ChavePixResponse(
                chavePix.getConta().getId(), chavePix.getTipo(), chavePix.getValor(), chavePix.getAtiva(), chavePix.getCriadaEm()
        );
    }

}
