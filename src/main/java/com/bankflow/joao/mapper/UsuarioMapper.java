package com.bankflow.joao.mapper;

import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.dto.response.UsuarioResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioMapper {

    public UsuarioResponse toDTO(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getStatus()
        );
    }

}
