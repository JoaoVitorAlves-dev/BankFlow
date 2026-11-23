package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.domain.roles.Role;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.GerenteRequest;
import com.bankflow.joao.dto.response.GerenteResponse;
import com.bankflow.joao.mapper.GerenteMapper;
import com.bankflow.joao.repository.AgenciaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GerenteService {

    private final GerenteRepository gerenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;

    public GerenteService(GerenteRepository gerenteRepository, UsuarioRepository usuarioRepository, AgenciaRepository agenciaRepository) {
        this.gerenteRepository = gerenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.agenciaRepository = agenciaRepository;
    }

    public GerenteResponse visualizarConta(Authentication auth) {
        return GerenteMapper.toDTO(gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado.")));
    }

    public Page<GerenteResponse> visualizar(Pageable pageable) {
        return gerenteRepository.findAll(pageable).map(GerenteMapper::toDTO);
    }

    public GerenteResponse criar(GerenteRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        Agencia agencia = agenciaRepository.findById(req.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agência não encontrada."));

        if (usuario.getRole() != Role.CLIENTE || usuario.getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O usuário precisa estar ativo e possuir perfil de cliente.");
        }

        Gerente entity = GerenteMapper.toEntity(req, usuario, agencia);
        return GerenteMapper.toDTO(gerenteRepository.save(entity));
    }

    public GerenteResponse atualizar(Long id, GerenteRequest req) {
        Gerente gerente = gerenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        Agencia agencia = agenciaRepository.findById(req.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agência não encontrada."));

        if (usuario.getRole() != Role.CLIENTE || usuario.getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.GONE, "O usuário não está ativo ou não possui perfil de cliente.");
        }

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível atualizar um gerente inativo.");
        }

        gerente.setUsuario(usuario);
        gerente.setMatricula(req.matricula());
        gerente.setAgencia(agencia);

        return GerenteMapper.toDTO(gerenteRepository.save(gerente));
    }

    public void desativar(Long id) {
        Gerente gerente = gerenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O gerente já está inativo.");
        }

        gerente.setAtivo(false);
        gerenteRepository.save(gerente);
    }

    public void deletar(Long id) {
        Gerente gerente = gerenteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível excluir um gerente inativo.");
        }

        gerenteRepository.delete(gerente);
    }

}