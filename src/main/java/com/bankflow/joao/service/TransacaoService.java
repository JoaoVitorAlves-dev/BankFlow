package com.bankflow.joao.service;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Transacao;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusTransacao;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.TransacaoRequest;
import com.bankflow.joao.dto.response.TransacaoResponse;
import com.bankflow.joao.mapper.TransacaoMapper;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final GerenteRepository gerenteRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository, GerenteRepository gerenteRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    @Transactional
    public Page<TransacaoResponse> listarTransacaoGerenteUsuario(Pageable pageable, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return transacaoRepository.findAllByOrigemAgenciaId(gerente.getAgencia().getId(), pageable).map(TransacaoMapper::toDTO);
    }

    @Transactional
    public Page<TransacaoResponse> listar(Pageable pageable) {
        return transacaoRepository.findAll(pageable).map(TransacaoMapper::toDTO);
    }

    @Transactional
    public TransacaoResponse criar(TransacaoRequest req) {
        Conta contaOrigem = contaRepository.findById(req.contaOrigemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));
        Conta contaDestino = contaRepository.findById(req.contaDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (contaOrigem.getUsuario().getStatus() != StatusUsuario.ATIVO || contaOrigem.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta de origem ou seu proprietário não está ativo.");
        }
        if (contaDestino.getUsuario().getStatus() != StatusUsuario.ATIVO || contaDestino.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta de destino ou seu proprietário não está ativo.");
        }
        if (contaOrigem.getSaldo().compareTo(req.valor()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "saldo insuficiente.");
        }

        contaDestino.setSaldo(contaDestino.getSaldo().add(req.valor()));
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(req.valor()));

        contaRepository.save(contaDestino);
        contaRepository.save(contaOrigem);

        Transacao entity = TransacaoMapper.toEntity(req, contaDestino, contaOrigem);
        entity.setStatus(StatusTransacao.CONCLUIDA);

        return TransacaoMapper.toDTO(transacaoRepository.save(entity));
    }

    @Transactional
    public Page<TransacaoResponse> listarTransacaoUsuario(Pageable pageable, Authentication auth) {
        return transacaoRepository.findAllByOrigemUsuarioEmail(auth.getName(), pageable).map(TransacaoMapper::toDTO);
    }

    @Transactional
    public TransacaoResponse criarTransacaoUsuario(TransacaoRequest req, Authentication auth) {
        Conta contaDestino = contaRepository.findById(req.contaDestinoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));
        Conta contaOrigem = contaRepository.findById(req.contaOrigemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!contaOrigem.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para realizar operações nesta conta.");
        }
        if (contaOrigem.getUsuario().getStatus() != StatusUsuario.ATIVO || contaOrigem.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta de origem ou seu proprietário não está ativo.");
        }
        if (contaDestino.getUsuario().getStatus() != StatusUsuario.ATIVO || contaDestino.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta de destino ou seu proprietário não está ativo.");
        }
        if (contaOrigem.getSaldo().compareTo(req.valor()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "saldo insuficiente.");
        }

        contaDestino.setSaldo(contaDestino.getSaldo().add(req.valor()));
        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(req.valor()));

        contaRepository.save(contaDestino);
        contaRepository.save(contaOrigem);

        Transacao entity = TransacaoMapper.toEntity(req, contaDestino, contaOrigem);
        entity.setStatus(StatusTransacao.CONCLUIDA);

        return TransacaoMapper.toDTO(transacaoRepository.save(entity));
    }

}


