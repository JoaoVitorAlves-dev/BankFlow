package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.ContaRequest;
import com.bankflow.joao.dto.response.ContaResponse;
import com.bankflow.joao.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping("/agencia")
    public ResponseEntity<Page<ContaResponse>> visualizarClientesGerenteAgencia(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(contaService.visualizarClientesGerenteAgencia(auth, pageable));
    }

    @GetMapping("/minha")
    public ResponseEntity<ContaResponse> verConta(Authentication auth) {
        return ResponseEntity.ok(contaService.verConta(auth));
    }

    @PostMapping("/agencia")
    public ResponseEntity<ContaResponse> criarGerentePermission(@RequestBody @Valid ContaRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.criarGerentePermission(req, auth));
    }

    @PatchMapping("/agencia/{id}")
    public ResponseEntity<ContaResponse> atualizarGerentePermission(@PathVariable Long id, @RequestBody @Valid ContaRequest req, Authentication auth) {
        return ResponseEntity.ok(contaService.atualizarGerentePermission(id, req, auth));
    }

    @DeleteMapping("/agencia/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, Authentication auth) {
        contaService.deleteById(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Page<ContaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(contaService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<ContaResponse> criar(@RequestBody @Valid ContaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ContaRequest req) {
        return ResponseEntity.ok(contaService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
