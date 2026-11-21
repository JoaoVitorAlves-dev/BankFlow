package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.dto.request.ContaRequest;
import com.bankflow.joao.dto.response.ContaResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContaMapper {

    public Conta toEntity(ContaRequest req, Agencia agencia, Usuario usuario) {
        Conta conta = new Conta();
        conta.setAgencia(agencia);
        conta.setLimite(req.limite());
        conta.setNumero(req.numero());
        conta.setTipo(req.tipo());
        conta.setUsuario(usuario);
        conta.setVersion(req.version());
        return conta;
    }

    public ContaResponse toDTO(Conta conta) {
        return new ContaResponse(
                conta.getUsuario().getId(), conta.getAgencia().getId(), conta.getNumero(), conta.getTipo(),
                conta.getSaldo(), conta.getLimite(), conta.getStatus(), conta.getVersion(), conta.getCriadaEm()
        );
    }

}
