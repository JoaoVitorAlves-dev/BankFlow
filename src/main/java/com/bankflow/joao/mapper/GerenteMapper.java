package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.dto.request.GerenteRequest;
import com.bankflow.joao.dto.response.GerenteResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GerenteMapper {

    public Gerente toEntity(GerenteRequest req, Usuario usuario, Agencia agencia) {
        Gerente gerente = new Gerente();
        gerente.setAgencia(agencia);
        gerente.setMatricula(req.matricula());
        gerente.setUsuario(usuario);
        return gerente;
    }

    public GerenteResponse toDTO(Gerente gerente) {
        return new GerenteResponse(
                gerente.getId(), gerente.getUsuario().getId(), gerente.getAgencia().getId(),
                gerente.getMatricula(), gerente.getAtivo()
        );
    }

}
