package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.domain.roles.Role;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.ContaRequest;
import com.bankflow.joao.dto.response.ContaResponse;
import com.bankflow.joao.mapper.ContaMapper;
import com.bankflow.joao.repository.AgenciaRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;
    private final GerenteRepository gerenteRepository;

    public ContaService(ContaRepository contaRepository, UsuarioRepository usuarioRepository, AgenciaRepository agenciaRepository, GerenteRepository gerenteRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.agenciaRepository = agenciaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<ContaResponse> visualizarClientesGerenteAgencia(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return contaRepository.findAllByAgenciaId(gerente.getAgencia().getId(), pageable);
    }

    public ContaResponse verConta(Authentication auth) {
        return ContaMapper.toDTO(contaRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public ContaResponse criarGerentePermission(ContaRequest req, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Agencia agencia = gerente.getAgencia();

        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }
        if (!usuario.getRole().equals(Role.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Conta entity = ContaMapper.toEntity(req, agencia, usuario);

        return ContaMapper.toDTO(contaRepository.save(entity));
    }

    public ContaResponse atualizarGerentePermission(Long id, ContaRequest req, Authentication auth) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!conta.getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!conta.getStatus().equals(StatusConta.ATIVA)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }
        if (!conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!conta.getUsuario().getRole().equals(Role.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        conta.setVersion(req.version());
        conta.setTipo(req.tipo());
        conta.setNumero(req.numero());
        conta.setLimite(req.limite());
        conta.setStatus(req.status());

        return ContaMapper.toDTO(contaRepository.save(conta));
    }

    public void deleteById(Long id, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!conta.getStatus().equals(StatusConta.ATIVA) || !conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }
        if (!conta.getUsuario().getRole().equals(Role.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!conta.getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        contaRepository.delete(conta);
    }

    public Page<ContaResponse> listar(Pageable pageable) {
        return contaRepository.findAll(pageable).map(ContaMapper::toDTO);
    }

    public ContaResponse criar(ContaRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Agencia agencia = agenciaRepository.findById(req.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!usuario.getRole().equals(Role.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        Conta entity = ContaMapper.toEntity(req, agencia, usuario);
        return ContaMapper.toDTO(contaRepository.save(entity));
    }

    public ContaResponse atualizar(Long id, ContaRequest request) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Agencia agencia = agenciaRepository.findById(request.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!conta.getStatus().equals(StatusConta.ATIVA) || !conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!conta.getUsuario().getRole().equals(Role.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        conta.setStatus(request.status());
        conta.setLimite(request.limite());
        conta.setNumero(request.numero());
        conta.setTipo(request.tipo());
        conta.setVersion(request.version());
        conta.setAgencia(agencia);

        return ContaMapper.toDTO(contaRepository.save(conta));
    }

    public void deletar(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!conta.getStatus().equals(StatusConta.ATIVA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        contaRepository.delete(conta);
    }

}
