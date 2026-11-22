package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Cartao;
import com.bankflow.joao.domain.Fatura;
import com.bankflow.joao.dto.request.FaturaRequest;
import com.bankflow.joao.dto.response.FaturaResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FaturaMapper {

    public Fatura toEntity(FaturaRequest req, Cartao cartao) {
        Fatura fatura = new Fatura();
        fatura.setCartao(cartao);
        fatura.setMesRef(req.mesRef());
        fatura.setValorTotal(req.valorTotal());
        fatura.setVencimento(req.vencimento());
        return fatura;
    }

    public FaturaResponse toDTO(Fatura fatura) {
        return new FaturaResponse(
                fatura.getCartao().getId(), fatura.getMesRef(), fatura.getValorTotal(), fatura.getVencimento(), fatura.getStatus()
        );
    }

}
