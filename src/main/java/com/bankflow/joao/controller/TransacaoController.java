package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.TransacaoRequest;
import com.bankflow.joao.dto.response.TransacaoResponse;
import com.bankflow.joao.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<TransacaoResponse>> listarTransacaoGerenteUsuario(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(transacaoService.listarTransacaoUsuario(pageable, auth));
    }

    @GetMapping
    public ResponseEntity<Page<TransacaoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(transacaoService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(@RequestBody @Valid TransacaoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.criar(req));
    }

    @PostMapping("/minhas")
    public ResponseEntity<TransacaoResponse> criarTransacaoUsuario(@RequestBody @Valid TransacaoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.criarTransacaoUsuario(req, auth));
    }

}
