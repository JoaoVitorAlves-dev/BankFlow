package com.bankflow.joao.service;

import com.bankflow.joao.domain.ChavePix;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.ChavePixRequest;
import com.bankflow.joao.dto.response.ChavePixResponse;
import com.bankflow.joao.mapper.ChavePixMapper;
import com.bankflow.joao.repository.ChavePixRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.service.qrcode.QrCodeService;
import org.hibernate.annotations.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class ChavePixService {

    private final ChavePixRepository chavePixRepository;
    private final ContaRepository contaRepository;
    private final GerenteRepository gerenteRepository;
    private final QrCodeService qrCodeService;

    public ChavePixService(ChavePixRepository chavePixRepository, ContaRepository contaRepository, GerenteRepository gerenteRepository, QrCodeService qrCodeService) {
        this.chavePixRepository = chavePixRepository;
        this.contaRepository = contaRepository;
        this.gerenteRepository = gerenteRepository;
        this.qrCodeService = qrCodeService;
    }

    public Page<ChavePixResponse> visualizar(Pageable pageable) {
        return chavePixRepository.findAll(pageable).map(ChavePixMapper::toDTO);
    }

    public ChavePixResponse criar(ChavePixRequest req) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        ChavePix entity = ChavePixMapper.toEntity(req, conta);

        if (entity.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO || entity.getConta().getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        return ChavePixMapper.toDTO(chavePixRepository.save(entity));
    }

    public ChavePixResponse atualizar(Long id, ChavePixRequest req) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ChavePix não encontrada."));
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getUsuario().getStatus() != StatusUsuario.ATIVO || conta.getStatus() != StatusConta.ATIVA
        || chavePix.getConta().getStatus() != StatusConta.ATIVA || !chavePix.getAtiva()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta, usuário ou chave Pix inativos.");
        }

        chavePix.setConta(conta);
        chavePix.setTipo(req.tipo());
        chavePix.setValor(req.valor());

        return ChavePixMapper.toDTO(chavePixRepository.save(chavePix));
    }

    public void deletar(Long id) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ChavePix não encontrada."));

        if (chavePix.getAtiva() || chavePix.getConta().getStatus() != StatusConta.ATIVA
                || chavePix.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Chave Pix ativa ou conta/usuário inativos.");
        }

        chavePixRepository.delete(chavePix);
    }

    public void desativar(Long id) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ChavePix não encontrada."));

        if (!chavePix.getAtiva() || chavePix.getConta().getStatus() != StatusConta.ATIVA
                || chavePix.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Chave Pix, conta ou usuário inativos.");
        }

        chavePix.setAtiva(false);
        chavePixRepository.save(chavePix);
    }

    public Page<ChavePixResponse> verChaveGerente(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));
        return chavePixRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable).map(ChavePixMapper::toDTO);
    }

    public Page<ChavePixResponse> verPropriasChaves(Authentication auth, Pageable pageable) {
        return chavePixRepository.findAllByContaUsuarioEmail(auth.getName(), pageable).map(ChavePixMapper::toDTO);
    }

    public ChavePixResponse criarPropriaChave(ChavePixRequest req, Authentication auth) {
        Conta conta = contaRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        ChavePix entity = ChavePixMapper.toEntity(req, conta);

        if (entity.getConta().getStatus() != StatusConta.ATIVA || entity.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        return ChavePixMapper.toDTO(chavePixRepository.save(entity));
    }

    public ChavePixResponse atualizarClienteChave(Long id, ChavePixRequest req, Authentication auth) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ChavePix não encontrada."));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!chavePix.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conta vinculada alterada.");
        }
        if (!chavePix.getConta().getUsuario().getEmail().equals(auth.getName()) || !conta.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta chave Pix.");
        }
        if (chavePix.getConta().getStatus() != StatusConta.ATIVA || chavePix.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO
                || conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta, usuário ou chave Pix inativos.");
        }

        chavePix.setConta(conta);
        chavePix.setValor(req.valor());
        chavePix.setTipo(req.tipo());

        return ChavePixMapper.toDTO(chavePixRepository.save(chavePix));
    }

    public void desativarClienteChave(Long id, Authentication auth) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ChavePix não encontrada."));

        if (!chavePix.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta chave Pix.");
        }
        if (chavePix.getConta().getStatus() != StatusConta.ATIVA || chavePix.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        chavePix.setAtiva(false);
        chavePixRepository.save(chavePix);
    }

    public void deletar(Long id, Authentication auth) {
        ChavePix chavePix = chavePixRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ChavePix não encontrada."));

        if (!chavePix.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }
        if (chavePix.getAtiva() || chavePix.getConta().getStatus() != StatusConta.ATIVA || chavePix.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Chave Pix ativa ou conta/usuário inativos.");
        }

        chavePixRepository.delete(chavePix);
    }

}
