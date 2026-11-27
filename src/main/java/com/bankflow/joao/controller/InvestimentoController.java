package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.InvestimentoRequest;
import com.bankflow.joao.dto.response.InvestimentoResponse;
import com.bankflow.joao.service.InvestimentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/investimentos")
public class InvestimentoController {

    private final InvestimentoService investimentoService;

    public InvestimentoController(InvestimentoService investimentoService) {
        this.investimentoService = investimentoService;
    }

    @PostMapping
    public ResponseEntity<InvestimentoResponse> criar(@RequestBody @Valid InvestimentoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(investimentoService.criar(req));
    }

    @PostMapping("/meus")
    public ResponseEntity<InvestimentoResponse> criarInvestimentoCliente(@RequestBody @Valid InvestimentoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(investimentoService.criarInvestimentoCliente(req, auth));
    }

    @GetMapping
    public ResponseEntity<Page<InvestimentoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(investimentoService.listar(pageable));
    }

    @GetMapping("/meus")
    public ResponseEntity<Page<InvestimentoResponse>> listarInvestimentoCliente(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(investimentoService.listarInvestimentoCliente(auth, pageable));
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<InvestimentoResponse>> listarInvestimentoGerentes(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(investimentoService.listarInvestimentoGerentes(auth, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InvestimentoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid InvestimentoRequest req) {
        return ResponseEntity.ok(investimentoService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        investimentoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/meus/{id}")
    public ResponseEntity<Void> deleteInvestimentoCliente(@PathVariable Long id, Authentication auth) {
        investimentoService.deleteInvestimentoCliente(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
