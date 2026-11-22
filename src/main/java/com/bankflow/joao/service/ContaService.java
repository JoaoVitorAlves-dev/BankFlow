package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.dto.request.ContaRequest;
import com.bankflow.joao.dto.response.ContaResponse;
import com.bankflow.joao.mapper.ContaMapper;
import com.bankflow.joao.repository.AgenciaRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;

    public ContaService(ContaRepository contaRepository, UsuarioRepository usuarioRepository, AgenciaRepository agenciaRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.agenciaRepository = agenciaRepository;
    }

    public ContaResponse verConta(Authentication auth) {
        return contaRepository.findByUsuarioEmail(auth.getName());
    }

    public Page<ContaResponse> listar(Pageable pageable) {
        return contaRepository.findAll(pageable).map(ContaMapper::toDTO);
    }

    public ContaResponse criar(ContaRequest req, Authentication auth) {
        Agencia agencia = agenciaRepository.findById(req.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


    }

}
