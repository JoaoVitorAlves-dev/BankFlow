package com.bankflow.joao.service;

import com.bankflow.joao.domain.Cartao;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.CartaoRequest;
import com.bankflow.joao.dto.response.CartaoResponse;
import com.bankflow.joao.mapper.CartaoMapper;
import com.bankflow.joao.repository.CartaoRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final ContaRepository contaRepository;
    private final GerenteRepository gerenteRepository;

    public CartaoService(CartaoRepository cartaoRepository, ContaRepository contaRepository, GerenteRepository gerenteRepository) {
        this.cartaoRepository = cartaoRepository;
        this.contaRepository = contaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<CartaoResponse> listarCartaoGerente(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));
        return cartaoRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable).map(CartaoMapper::toDTO);
    }

    public CartaoResponse criarCartaoGerente(CartaoRequest req, Authentication auth) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrado."));

        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        if (!conta.getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "A conta não pertence à sua agência.");
        }
        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        Cartao entity = CartaoMapper.toEntity(req, conta);
        return CartaoMapper.toDTO(cartaoRepository.save(entity));
    }

    public CartaoResponse atualizarCartaoGerente(Long id, CartaoRequest req, Authentication auth) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        if (!cartao.getConta().getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "A conta não pertence à sua agência.");
        }

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!cartao.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O cartão informado não pertence a esta conta.");
        }
        if (!cartao.getAtivo() || cartao.getConta().getStatus() != StatusConta.ATIVA || cartao.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Operação não permitida: cartão, conta ou usuário inativo.");
        }

        cartao.setValidade(req.validade());
        cartao.setUltimos4(req.ultimos4());
        cartao.setLimite(req.limite());
        cartao.setBandeira(req.bandeira());

        return CartaoMapper.toDTO(cartaoRepository.save(cartao));
    }

    public void desativarCartaoGerente(Long id, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        if (!cartao.getConta().getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "A conta não pertence à sua agência.");
        }
        if (!cartao.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Cartão inativo.");
        }

        cartao.setAtivo(false);
        cartaoRepository.save(cartao);
    }

    public void deletarCartaoGerente(Long id, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        if (!cartao.getConta().getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "A conta não pertence à sua agência.");
        }
        if (cartao.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Cartão ativo.");
        }

        cartaoRepository.delete(cartao);
    }

    public Page<CartaoResponse> listarCartaoCliente(Authentication auth, Pageable pageable) {
        return cartaoRepository.findAllByContaUsuarioEmail(auth.getName(), pageable).map(CartaoMapper::toDTO);
    }

    public CartaoResponse atualizarProprioCartao(Long id, CartaoRequest req, Authentication auth) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!cartao.getConta().getUsuario().getEmail().equals(auth.getName()) || !conta.getUsuario().getEmail().equals(auth.getName())
                || !cartao.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso.");
        }
        if (!cartao.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Cartão inativo.");
        }

        cartao.setBandeira(req.bandeira());
        cartao.setLimite(req.limite());
        cartao.setUltimos4(req.ultimos4());
        cartao.setValidade(req.validade());

        return CartaoMapper.toDTO(cartaoRepository.save(cartao));
    }

    public Page<CartaoResponse> listar(Pageable pageable) {
        return cartaoRepository.findAll(pageable).map(CartaoMapper::toDTO);
    }

    public CartaoResponse criar(CartaoRequest req) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getUsuario().getStatus() != StatusUsuario.ATIVO || conta.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        Cartao entity = CartaoMapper.toEntity(req, conta);
        return CartaoMapper.toDTO(cartaoRepository.save(entity));
    }

    public CartaoResponse atualizar(Long id, CartaoRequest req) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrada."));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!cartao.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O cartão informado não pertence a esta conta.");
        }
        if (!cartao.getAtivo() || cartao.getConta().getStatus() != StatusConta.ATIVA
                || cartao.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Operação não permitida: cartão, conta ou usuário inativo.");
        }

        cartao.setValidade(req.validade());
        cartao.setUltimos4(req.ultimos4());
        cartao.setLimite(req.limite());
        cartao.setBandeira(req.bandeira());

        return CartaoMapper.toDTO(cartaoRepository.save(cartao));
    }

    public void deletar(Long id) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        if (cartao.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Cartão ativo.");
        }

        cartaoRepository.delete(cartao);
    }

    public void desativar(Long id) {
        Cartao cartao = cartaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão não encontrado."));

        if (!cartao.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Cartão inativo.");
        }

        cartao.setAtivo(false);
        cartaoRepository.delete(cartao);
    }
}