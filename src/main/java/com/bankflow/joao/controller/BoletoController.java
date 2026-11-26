package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.BoletoRequest;
import com.bankflow.joao.dto.response.BoletoResponse;
import com.bankflow.joao.service.BoletoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

    private final BoletoService boletoService;

    public BoletoController(BoletoService boletoService) {
        this.boletoService = boletoService;
    }

    @GetMapping("/me")
    public ResponseEntity<Page<BoletoResponse>> verBoletosClientes(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(boletoService.verBoletosClientes(auth, pageable));
    }

    @PostMapping("/me")
    public ResponseEntity<BoletoResponse> criarBoletoCliente(@RequestBody @Valid BoletoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletoService.criarBoletoCliente(req, auth));
    }

    @DeleteMapping("/{id}/me")
    public ResponseEntity<Void> deletarBoletoCliente(@PathVariable Long id, Authentication auth) {
        boletoService.deletarBoletoCliente(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<BoletoResponse>> listarGerente(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(boletoService.listarGerente(auth, pageable));
    }

    @GetMapping
    public ResponseEntity<Page<BoletoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(boletoService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<BoletoResponse> criar(@RequestBody @Valid BoletoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletoService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoletoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid BoletoRequest req) {
        return ResponseEntity.ok(boletoService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boletoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
