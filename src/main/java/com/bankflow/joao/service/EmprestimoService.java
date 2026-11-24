package com.bankflow.joao.service;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Emprestimo;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusEmprestimo;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.EmprestimoRequest;
import com.bankflow.joao.dto.response.EmprestimoResponse;
import com.bankflow.joao.mapper.EmprestimoMapper;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.EmprestimoRepository;
import com.bankflow.joao.repository.GerenteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final GerenteRepository gerenteRepository;
    private final ContaRepository contaRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, GerenteRepository gerenteRepository, ContaRepository contaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.gerenteRepository = gerenteRepository;
        this.contaRepository = contaRepository;
    }

    public Page<EmprestimoResponse> listarEmprestimoClienteUsuario(Authentication auth, Pageable pageable) {
        return emprestimoRepository.findAllByContaUsuarioEmail(auth.getName(), pageable)
                .map(EmprestimoMapper::toDTO);
    }

    public EmprestimoResponse criarEmprestimoClienteUsuario(EmprestimoRequest req, Authentication auth) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!conta.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta conta.");
        }
        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta ou o usuário não está ativo.");
        }

        Emprestimo entity = EmprestimoMapper.toEntity(req, conta);
        entity.setStatusEmprestimo(StatusEmprestimo.SOLICITADO);

        return EmprestimoMapper.toDTO(emprestimoRepository.save(entity));
    }

    public void deletarEmprestimoClienteUsuario(Long id, Authentication auth) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não encontrado."));

        if (!emprestimo.getConta().getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar esta conta.");
        }
        if (emprestimo.getStatusEmprestimo() == StatusEmprestimo.SOLICITADO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O empréstimo está com o status SOLICITADO.");
        }

        emprestimoRepository.delete(emprestimo);
    }

    public Page<EmprestimoResponse> listarEmprestimoGerenteClienteUsuario(Pageable pageable, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        return emprestimoRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable)
                .map(EmprestimoMapper::toDTO);
    }

    public EmprestimoResponse atualizarEmprestimoGerenteClienteUsuario(Long id, EmprestimoRequest req, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não encontrado."));

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente está desativado.");
        }
        if (!emprestimo.getConta().getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente não tem permissão para acessar empréstimos de outra agência.");
        }
        if (emprestimo.getStatusEmprestimo() == StatusEmprestimo.RECUSADO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não é possível realizar operações em um empréstimo recusado.");
        }
        if (emprestimo.getConta().getStatus() != StatusConta.ATIVA || emprestimo.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta ou o usuário não está ativo.");
        }

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!emprestimo.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O empréstimo não pertence à conta informada.");
        }

        emprestimo.setQtdParcelas(req.qtdParcelas());
        emprestimo.setTaxa_juros(req.taxaJuros());
        emprestimo.setValor(req.valor());

        return EmprestimoMapper.toDTO(emprestimoRepository.save(emprestimo));
    }

    public Page<EmprestimoResponse> listar(Pageable pageable) {
        return emprestimoRepository.findAll(pageable)
                .map(EmprestimoMapper::toDTO);
    }

    public EmprestimoResponse criar(EmprestimoRequest req) {
        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != StatusConta.ATIVA || conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta ou o usuário não está ativo.");
        }

        Emprestimo entity = EmprestimoMapper.toEntity(req, conta);
        entity.setStatusEmprestimo(StatusEmprestimo.SOLICITADO);

        return EmprestimoMapper.toDTO(emprestimoRepository.save(entity));
    }

    public EmprestimoResponse atualizar(Long id, EmprestimoRequest req) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não encontrado."));

        Conta conta = contaRepository.findById(req.contaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (!emprestimo.getConta().equals(conta)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O emprestimo não pertence a conta informado.");
        }
        if (emprestimo.getStatusEmprestimo() != StatusEmprestimo.SOLICITADO ||
                emprestimo.getConta().getStatus() != StatusConta.ATIVA || emprestimo.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O empréstimo não está solicitado ou a conta/usuário não está ativo.");
        }

        emprestimo.setValor(req.valor());
        emprestimo.setTaxa_juros(req.taxaJuros());
        emprestimo.setQtdParcelas(req.qtdParcelas());

        return EmprestimoMapper.toDTO(emprestimoRepository.save(emprestimo));
    }

    public void deletar(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não encontrado."));

        if (emprestimo.getStatusEmprestimo() == StatusEmprestimo.SOLICITADO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O empréstimo está com o status SOLICITADO.");
        }

        emprestimoRepository.delete(emprestimo);
    }

}
