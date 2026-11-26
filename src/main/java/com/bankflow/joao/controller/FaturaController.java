package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.FaturaRequest;
import com.bankflow.joao.dto.response.FaturaResponse;
import com.bankflow.joao.service.FaturaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faturas")
public class FaturaController {

    private final FaturaService faturaService;

    public FaturaController(FaturaService faturaService) {
        this.faturaService = faturaService;
    }

    @GetMapping("/minhas")
    public ResponseEntity<Page<FaturaResponse>> listarClienteFaturas(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(faturaService.listarClienteFaturas(auth, pageable));
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<FaturaResponse>> listarClientesFaturasGerente(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(faturaService.listarClientesFaturasGerente(auth, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<FaturaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(faturaService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<FaturaResponse> criar(@RequestBody @Valid FaturaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faturaService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FaturaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid FaturaRequest req) {
        return ResponseEntity.ok(faturaService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        faturaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT) .build();
    }

}
