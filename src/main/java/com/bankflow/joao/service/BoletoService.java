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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!conta.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!conta.getStatus().equals(StatusConta.ATIVA) || !conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Boleto entity = BoletoMapper.toEntity(req, conta);
        return BoletoMapper.toDTO(boletoRepository.save(entity));
    }

    public void deletarBoletoCliente(Long id, Authentication auth) {
        Boleto boleto = boletoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!boleto.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (boleto.getStatus().equals(StatusBoleto.EMITIDO)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!conta.getStatus().equals(StatusConta.ATIVA) || !conta.getUsuario().getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Boleto entity = BoletoMapper.toEntity(req, conta);
        return BoletoMapper.toDTO(boletoRepository.save(entity));
    }

    public BoletoResponse atualizar(Long id, BoletoRequest req) {
        Boleto boleto = boletoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!boleto.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (!boleto.getConta().getUsuario().getStatus().equals(StatusUsuario.ATIVO) || !boleto.getConta().getStatus().equals(StatusConta.ATIVA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        boleto.setVencimento(req.vencimento());
        boleto.setValor(req.valor());
        boleto.setCodigoBarras(req.codigoBarras());

        return BoletoMapper.toDTO(boletoRepository.save(boleto));
    }

    public void deletar(Long id) {
        Boleto boleto = boletoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (boleto.getStatus().equals(StatusBoleto.EMITIDO)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        boletoRepository.delete(boleto);
    }

}
