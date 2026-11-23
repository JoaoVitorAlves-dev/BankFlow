package com.bankflow.joao.service;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Investimento;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.InvestimentoRequest;
import com.bankflow.joao.dto.response.InvestimentoResponse;
import com.bankflow.joao.mapper.InvestimentoMapper;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.InvestimentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class InvestimentoService {
    private final InvestimentoRepository investimentoRepository;

    private final ContaRepository contaRepository;
    private final GerenteRepository gerenteRepository;

    public InvestimentoService(InvestimentoRepository investimentoRepository, ContaRepository contaRepository, GerenteRepository gerenteRepository) {
        this.investimentoRepository = investimentoRepository;
        this.contaRepository = contaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public InvestimentoResponse criar(InvestimentoRequest req) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta está desativada ou o usuário não está ativo.");
        }

        Investimento entity = InvestimentoMapper.toEntity(req, conta);

        if (entity.getConta().getSaldo().compareTo(entity.getValorAplicado()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Saldo insuficiente para realizar a aplicação.");
        }

        return InvestimentoMapper.toDTO(investimentoRepository.save(entity));
    }

    public InvestimentoResponse criarInvestimentoCliente(InvestimentoRequest req, Authentication auth) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!conta.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para realizar aplicações nesta conta.");
        }
        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta está desativada ou o usuário não está ativo.");
        }
        if (conta.getSaldo().compareTo(req.valorAplicado()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Saldo insuficiente para realizar a aplicação.");
        }

        Investimento entity = InvestimentoMapper.toEntity(req, conta);
        return InvestimentoMapper.toDTO(investimentoRepository.save(entity));
    }

    public Page<InvestimentoResponse> listar(Pageable pageable) {
        return investimentoRepository.findAll(pageable).map(InvestimentoMapper::toDTO);
    }

    public Page<InvestimentoResponse> listarInvestimentoCliente(Authentication auth, Pageable pageable) {
        return investimentoRepository.findAllByContaUsuarioEmail(auth.getName(), pageable).map(InvestimentoMapper::toDTO);
    }

    public Page<InvestimentoResponse> listarInvestimentoGerentes(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        return investimentoRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable)
                .map(InvestimentoMapper::toDTO);
    }

    public InvestimentoResponse atualizar(Long id, InvestimentoRequest req) {
        Investimento investimento = investimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento não encontrado."));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!investimento.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O investimento não pertence à conta informada.");
        }
        if (conta.getSaldo().compareTo(req.valorAplicado()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Saldo insuficiente para realizar a aplicação.");
        }

        investimento.setVencimento(req.vencimento());
        investimento.setValorAplicado(req.valorAplicado());
        investimento.setTipo(req.tipo());
        investimento.setTaxaAnual(req.taxaAnual());

        return InvestimentoMapper.toDTO(investimentoRepository.save(investimento));
    }

    public void deletar(Long id) {
        Investimento investimento = investimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento não encontrado."));

        investimentoRepository.delete(investimento);
    }

    public void deleteInvestimentoCliente(Long id, Authentication auth) {
        Investimento investimento = investimentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Investimento não encontrado."));

        if (!investimento.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para excluir este investimento.");
        }

        investimentoRepository.delete(investimento);
    }
}