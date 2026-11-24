package com.bankflow.joao.service;

import com.bankflow.joao.domain.Emprestimo;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Parcela;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusEmprestimo;
import com.bankflow.joao.domain.roles.StatusParcela;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.ParcelaRequest;
import com.bankflow.joao.dto.response.ParcelaResponse;
import com.bankflow.joao.mapper.ParcelaMapper;
import com.bankflow.joao.repository.EmprestimoRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.ParcelaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ParcelaService {

    private final ParcelaRepository parcelaRepository;
    private final GerenteRepository gerenteRepository;
    private final EmprestimoRepository emprestimoRepository;

    public ParcelaService(ParcelaRepository parcelaRepository, GerenteRepository gerenteRepository, EmprestimoRepository emprestimoRepository) {
        this.parcelaRepository = parcelaRepository;
        this.gerenteRepository = gerenteRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public Page<ParcelaResponse> listarParcelaClienteUsuario(Pageable pageable, Authentication auth) {
        return parcelaRepository.findAllByEmprestimoContaUsuarioEmail(auth.getName(), pageable)
                .map(ParcelaMapper::toDTO);
    }

    public Page<ParcelaResponse> listarParcelaGerenteClienteUsuario(Pageable pageable, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        return parcelaRepository.findAllByEmprestimoContaAgenciaId(gerente.getAgencia().getId(), pageable)
                .map(ParcelaMapper::toDTO);
    }

    public Page<ParcelaResponse> listar(Pageable pageable) {
        return parcelaRepository.findAll(pageable)
                .map(ParcelaMapper::toDTO);
    }

    public ParcelaResponse criar(ParcelaRequest req) {
        Emprestimo emprestimo = emprestimoRepository.findById(req.emprestimoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não encontrado."));

        if (emprestimo.getStatusEmprestimo() != StatusEmprestimo.APROVADO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O empréstimo não está aprovado.");
        }
        if (emprestimo.getConta().getStatus() != StatusConta.ATIVA || emprestimo.getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta ou o usuário não está ativo.");
        }

        Parcela entity = ParcelaMapper.toEntity(req, emprestimo);
        entity.setStatus(StatusParcela.PENDENTE);

        return ParcelaMapper.toDTO(parcelaRepository.save(entity));
    }

    public ParcelaResponse atualizar(Long id, ParcelaRequest req) {
        Parcela parcela = parcelaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parcela não encontrada."));

        Emprestimo emprestimo = emprestimoRepository.findById(req.emprestimoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprestimo não encontrado."));

        if (!parcela.getEmprestimo().equals(emprestimo)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A parcela não pertence ao empréstimo informado.");
        }
        if (parcela.getStatus() == StatusParcela.PAGA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,  "A parcela já está paga.");
        }
        if (parcela.getEmprestimo().getStatusEmprestimo() != StatusEmprestimo.APROVADO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O empréstimo não está aprovado.");
        }
        if (parcela.getEmprestimo().getConta().getStatus() != StatusConta.ATIVA || parcela.getEmprestimo().getConta().getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta ou o usuário não está ativo.");
        }

        parcela.setVencimento(req.vencimento());
        parcela.setValor(req.valor());
        parcela.setNumero(req.numero());

        return ParcelaMapper.toDTO(parcelaRepository.save(parcela));
    }

    public void deletar(Long id) {
        Parcela parcela = parcelaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parcela não encontrada."));

        if (parcela.getStatus() != StatusParcela.PAGA) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A parcela ainda não foi paga.");
        }

        parcelaRepository.delete(parcela);
    }

}
