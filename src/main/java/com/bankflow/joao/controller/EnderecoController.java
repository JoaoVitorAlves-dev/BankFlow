package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.EnderecoRequest;
import com.bankflow.joao.dto.response.EnderecoResponse;
import com.bankflow.joao.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoResponse>> listarTodos(Pageable pageable) {
        return ResponseEntity.ok(enderecoService.listarTodos(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<Page<EnderecoResponse>> listar(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(enderecoService.listar(pageable, auth));
    }

    @PostMapping
    public ResponseEntity<EnderecoResponse> criar(@RequestBody @Valid EnderecoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.criar(req));
    }

    @PostMapping("/me")
    public ResponseEntity<EnderecoResponse> criarProprio(@RequestBody @Valid EnderecoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoService.criarProprio(req, auth));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EnderecoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EnderecoRequest req) {
        return ResponseEntity.ok(enderecoService.atualizar(id, req));
    }

    @PatchMapping("/me/{id}")
    public ResponseEntity<EnderecoResponse> atualizarProprio(@PathVariable Long id, @RequestBody @Valid EnderecoRequest req, Authentication auth) {
        return ResponseEntity.ok(enderecoService.atualizarProprio(id, req, auth));
    }

    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> deletarProprio(@PathVariable Long id, Authentication auth) {
        enderecoService.deletarProprio(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
