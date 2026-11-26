package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.ParcelaRequest;
import com.bankflow.joao.dto.response.ParcelaResponse;
import com.bankflow.joao.service.ParcelaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parcelas")
public class ParcelaController {

    private final ParcelaService parcelaService;

    public ParcelaController(ParcelaService parcelaService) {
        this.parcelaService = parcelaService;
    }

    @GetMapping("/minhas")
    public ResponseEntity<Page<ParcelaResponse>> listarParcelaClienteUsuario(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(parcelaService.listarParcelaClienteUsuario(pageable, auth));
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<ParcelaResponse>> listarParcelaGerenteClienteUsuario(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(parcelaService.listarParcelaClienteUsuario(pageable, auth));
    }

    @GetMapping
    public ResponseEntity<Page<ParcelaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(parcelaService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<ParcelaResponse> criar(@RequestBody @Valid ParcelaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parcelaService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ParcelaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ParcelaRequest req) {
        return ResponseEntity.ok(parcelaService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        parcelaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
