package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.dto.request.AgenciaRequest;
import com.bankflow.joao.dto.response.AgenciaResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AgenciaMapper {

    public Agencia toEntity(AgenciaRequest req) {
        Agencia agencia = new Agencia();
        agencia.setNome(req.nome());
        agencia.setCidade(req.cidade());
        agencia.setCodigo(req.codigo());
        agencia.setEstado(req.estado());
        return agencia;
    }

    public AgenciaResponse toDTO(Agencia agencia) {
        return new AgenciaResponse(
                agencia.getNome(), agencia.getCidade(), agencia.getCodigo(), agencia.getEstado()
        );
    }

}
