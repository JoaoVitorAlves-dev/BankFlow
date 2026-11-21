package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Cartao;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.dto.request.CartaoRequest;
import com.bankflow.joao.dto.response.CartaoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CartaoMapper {

    public Cartao toEntity(CartaoRequest req, Conta conta) {
        Cartao cartao = new Cartao();
        cartao.setBandeira(req.bandeira());
        cartao.setConta(conta);
        cartao.setLimite(req.limite());
        cartao.setUltimos4(req.ultimos4());
        cartao.setValidade(req.validade());
        return cartao;
    }

    public CartaoResponse toDTO(Cartao cartao) {
        return new CartaoResponse(
                cartao.getConta().getId(), cartao.getUltimos4(), cartao.getBandeira(), cartao.getValidade(), cartao.getLimite(), cartao.getAtivo()
        );
    }

}
