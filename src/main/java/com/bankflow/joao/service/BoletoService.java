package com.bankflow.joao.service;

import com.bankflow.joao.domain.Boleto;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.roles.StatusBoleto;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.BoletoRequest;
import com.bankflow.joao.dto.response.BoletoResponse;
import com.bankflow.joao.mapper.BoletoMapper;
import com.bankflow.joao.repository.BoletoRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BoletoService {

    private final BoletoRepository boletoRepository;
    private final ContaRepository contaRepository;
    private final GerenteRepository gerenteRepository;

    public BoletoService(BoletoRepository boletoRepository, ContaRepository contaRepository, GerenteRepository gerenteRepository) {
        this.boletoRepository = boletoRepository;
        this.contaRepository = contaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<BoletoResponse> verBoletosClientes(Authentication auth, Pageable pageable) {
        return boletoRepository.findAllByContaUsuarioEmail(auth.getName(), pageable).map(BoletoMapper::toDTO);
    }

    public BoletoResponse criarBoletoCliente(BoletoRequest req, Authentication auth) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrado."));

        if (!conta.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito de dados.");
        }
        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        Boleto entity = BoletoMapper.toEntity(req, conta);
        return BoletoMapper.toDTO(boletoRepository.save(entity));
    }

    public void deletarBoletoCliente(Long id, Authentication auth) {
        Boleto boleto = boletoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleto não encontrado."));

        if (!boleto.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso.");
        }
        if (boleto.getStatus() == StatusBoleto.EMITIDO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O boleto já foi emitido.");
        }

        boletoRepository.delete(boleto);
    }

    public Page<BoletoResponse> listarGerente(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return boletoRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable).map(BoletoMapper::toDTO);
    }

    public Page<BoletoResponse> listar(Pageable pageable) {
        return boletoRepository.findAll(pageable).map(BoletoMapper::toDTO);
    }

    public BoletoResponse criar(BoletoRequest req) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        Boleto entity = BoletoMapper.toEntity(req, conta);
        return BoletoMapper.toDTO(boletoRepository.save(entity));
    }

    public BoletoResponse atualizar(Long id, BoletoRequest req) {
        Boleto boleto = boletoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleto não encontrado."));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!boleto.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O boleto informado não pertence a esta conta.");
        }
        if (boleto.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO || boleto.getConta().getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Conta ou usuário inativo.");
        }

        boleto.setVencimento(req.vencimento());
        boleto.setValor(req.valor());
        boleto.setCodigoBarras(req.codigoBarras());

        return BoletoMapper.toDTO(boletoRepository.save(boleto));
    }

    public void deletar(Long id) {
        Boleto boleto = boletoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boleto não encontrado."));

        if (boleto.getStatus() == StatusBoleto.EMITIDO) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O boleto já foi emitido.");
        }

        boletoRepository.delete(boleto);
    }

}