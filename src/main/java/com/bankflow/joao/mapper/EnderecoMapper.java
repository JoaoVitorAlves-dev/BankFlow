package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Endereco;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.dto.request.EnderecoRequest;
import com.bankflow.joao.dto.response.EnderecoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnderecoMapper {

    public Endereco toEntity(EnderecoRequest req, Usuario usuario) {
        Endereco endereco = new Endereco();
        endereco.setRua(req.rua());
        endereco.setCep(req.cep());
        endereco.setCidade(req.cidade());
        endereco.setEstado(req.estado());
        return endereco;
    }

    public EnderecoResponse toDTO(Endereco endereco) {
        return new EnderecoResponse(endereco.getRua(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
    }

}
