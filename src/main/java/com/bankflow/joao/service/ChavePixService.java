package com.bankflow.joao.service;

import com.bankflow.joao.domain.ChavePix;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.roles.Role;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.ChavePixRequest;
import com.bankflow.joao.dto.response.ChavePixResponse;
import com.bankflow.joao.mapper.ChavePixMapper;
import com.bankflow.joao.repository.ChavePixRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChavePixService {

    private final ChavePixRepository chavePixRepository;
    private final ContaRepository contaRepository;
    private final GerenteRepository gerenteRepository;

    public ChavePixService(ChavePixRepository chavePixRepository, ContaRepository contaRepository, GerenteRepository gerenteRepository) {
        this.chavePixRepository = chavePixRepository;
        this.contaRepository = contaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<ChavePixResponse> visualizar(Pageable pageable) {
        return chavePixRepository.findAll(pageable).map(ChavePixMapper::toDTO);
    }

    public ChavePixResponse criar(ChavePixRequest req) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ChavePix entity = ChavePixMapper.toEntity(req, conta);

        if (!entity.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO) || !entity.getConta().getStatus().equals(StatusConta.ATIVA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return ChavePixMapper.toDTO(chavePixRepository.save(entity));
    }

    public ChavePixResponse atualizar(Long id, ChavePixRequest req) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO) || !conta.getStatus().equals(StatusConta.ATIVA)
        || !chavePix.getConta().getStatus().equals(StatusConta.ATIVA) || !chavePix.getAtiva()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        chavePix.setConta(conta);
        chavePix.setTipo(req.tipo());
        chavePix.setValor(req.valor());

        return ChavePixMapper.toDTO(chavePixRepository.save(chavePix));
    }

    public void deletar(Long id) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chavePix.getAtiva() || !chavePix.getConta().getStatus().equals(StatusConta.ATIVA)
                || !chavePix.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        chavePixRepository.delete(chavePix);
    }

    public void desativar(Long id) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chavePix.getAtiva() || !chavePix.getConta().getStatus().equals(StatusConta.ATIVA)
                || !chavePix.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        chavePix.setAtiva(false);
        chavePixRepository.save(chavePix);
    }

    public Page<ChavePixResponse> verChaveGerente(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return chavePixRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable).map(ChavePixMapper::toDTO);
    }

    public Page<ChavePixResponse> verPropriasChaves(Authentication auth, Pageable pageable) {
        return chavePixRepository.findAllByContaUsuarioEmail(auth.getName(), pageable).map(ChavePixMapper::toDTO);
    }

    public ChavePixResponse criarPropriaChave(ChavePixRequest req, Authentication auth) {
        Conta conta = contaRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ChavePix entity = ChavePixMapper.toEntity(req, conta);

        if (!entity.getConta().getStatus().equals(StatusConta.ATIVA) || !entity.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        return ChavePixMapper.toDTO(chavePixRepository.save(entity));
    }

    public ChavePixResponse atualizarClienteChave(Long id, ChavePixRequest req, Authentication auth) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chavePix.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!chavePix.getConta().getStatus().equals(StatusConta.ATIVA) || !chavePix.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO)
                || !conta.getStatus().equals(StatusConta.ATIVA) || !conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        chavePix.setConta(conta);
        chavePix.setValor(req.valor());
        chavePix.setTipo(req.tipo());

        return ChavePixMapper.toDTO(chavePixRepository.save(chavePix));
    }

    public void desativarClienteChave(Long id, Authentication auth) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chavePix.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!chavePix.getConta().getStatus().equals(StatusConta.ATIVA) || !chavePix.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        chavePix.setAtiva(false);
        chavePixRepository.save(chavePix);
    }

    public void deletar(Long id, Authentication auth) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chavePix.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!chavePix.getConta().getStatus().equals(StatusConta.ATIVA) || !chavePix.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.GONE);
        }

        chavePixRepository.delete(chavePix);
    }

}
