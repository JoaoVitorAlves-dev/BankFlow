package com.bankflow.joao.controller;

import com.bankflow.joao.dto.response.LancamentoResponse;
import com.bankflow.joao.service.LancamentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @GetMapping
    public ResponseEntity<Page<LancamentoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(lancamentoService.listar(pageable));
    }

    @GetMapping("/meus")
    public ResponseEntity<Page<LancamentoResponse>> listarLancamentoUsuario(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(lancamentoService.listarLancamentoUsuario(pageable, auth));
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<LancamentoResponse>> listarLancamentoGerente(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(lancamentoService.listarLancamentoGerente(pageable, auth));
    }

}
