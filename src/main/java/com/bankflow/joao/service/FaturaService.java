package com.bankflow.joao.service;

import com.bankflow.joao.domain.Cartao;
import com.bankflow.joao.domain.Fatura;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusFatura;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.FaturaRequest;
import com.bankflow.joao.dto.response.FaturaResponse;
import com.bankflow.joao.mapper.FaturaMapper;
import com.bankflow.joao.repository.CartaoRepository;
import com.bankflow.joao.repository.FaturaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final CartaoRepository cartaoRepository;
    private final GerenteRepository gerenteRepository;

    public FaturaService(FaturaRepository faturaRepository, CartaoRepository cartaoRepository, GerenteRepository gerenteRepository) {
        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<FaturaResponse> listarClienteFaturas(Authentication auth, Pageable pageable) {
        return faturaRepository.findAllByCartaoContaUsuarioEmail(auth.getName(), pageable)
                .map(FaturaMapper::toDTO);
    }

    public Page<FaturaResponse> listarClientesFaturasGerente(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        return faturaRepository.findAllByCartaoContaAgenciaId(gerente.getAgencia().getId(), pageable)
                .map(FaturaMapper::toDTO);
    }

    public Page<FaturaResponse> listar(Pageable pageable) {
        return faturaRepository.findAll(pageable)
                .map(FaturaMapper::toDTO);
    }

    public FaturaResponse criar(FaturaRequest req) {
        Cartao cartao = cartaoRepository.findById(req.cartaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        if (!cartao.getAtivo() || cartao.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO
                || cartao.getConta().getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O cartão está desativado ou a conta/usuário não está ativo.");
        }

        Fatura entity = FaturaMapper.toEntity(req, cartao);
        entity.setStatus(StatusFatura.ABERTA);
        return FaturaMapper.toDTO(faturaRepository.save(entity));
    }

    public FaturaResponse atualizar(Long id, FaturaRequest req) {
        Fatura fatura = faturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatura não encontrada."));

        Cartao cartao = cartaoRepository.findById(req.cartaoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        if (!fatura.getCartao().equals(cartao)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A fatura não pertence ao cartão informado.");
        }
        if (fatura.getVencimento().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fatura vencida.");
        }
        if (fatura.getStatus() == StatusFatura.FECHADA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A fatura está fechada.");
        }
        if (!fatura.getCartao().getAtivo() || fatura.getCartao().getConta().getStatus() != StatusConta.ATIVA
                || fatura.getCartao().getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O cartão está desativado ou a conta/usuário não está ativo.");
        }

        fatura.setVencimento(req.vencimento());
        fatura.setValorTotal(req.valorTotal());
        fatura.setMesRef(req.mesRef());

        return FaturaMapper.toDTO(faturaRepository.save(fatura));
    }

    public void deletar(Long id) {
        Fatura fatura = faturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fatura não encontrada."));

        if (fatura.getStatus() != StatusFatura.ABERTA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A fatura ainda está aberta.");
        }

        faturaRepository.delete(fatura);
    }

}
