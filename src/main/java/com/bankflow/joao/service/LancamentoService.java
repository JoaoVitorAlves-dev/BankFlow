package com.bankflow.joao.service;

import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.dto.response.LancamentoResponse;
import com.bankflow.joao.mapper.LancamentoMapper;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.LancamentoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final GerenteRepository gerenteRepository;

    public LancamentoService(LancamentoRepository lancamentoRepository, GerenteRepository gerenteRepository) {
        this.lancamentoRepository = lancamentoRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<LancamentoResponse> listar(Pageable pageable) {
        return lancamentoRepository.findAll(pageable)
                .map(LancamentoMapper::toDTO);
    }

    public Page<LancamentoResponse> listarLancamentoUsuario(Pageable pageable, Authentication auth) {
        return lancamentoRepository.findAllByContaUsuarioEmail(auth.getName(), pageable)
                .map(LancamentoMapper::toDTO);
    }

    public Page<LancamentoResponse> listarLancamentoGerente(Pageable pageable, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente está desativado.");
        }

        return lancamentoRepository.findAllByContaAgenciaId(gerente.getAgencia().getId(), pageable)
                .map(LancamentoMapper::toDTO);
    }

}
