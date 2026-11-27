package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.ChavePixRequest;
import com.bankflow.joao.dto.response.ChavePixResponse;
import com.bankflow.joao.service.ChavePixService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chavepix")
public class ChavePixController {

    private final ChavePixService chavePixService;

    public ChavePixController(ChavePixService chavePixService) {
        this.chavePixService = chavePixService;
    }

    @GetMapping
    public ResponseEntity<Page<ChavePixResponse>> visualizar(Pageable pageable) {
        return ResponseEntity.ok(chavePixService.visualizar(pageable));
    }

    @PostMapping
    public ResponseEntity<ChavePixResponse> criar(@RequestBody @Valid ChavePixRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chavePixService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChavePixResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ChavePixRequest req) {
        return ResponseEntity.ok(chavePixService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        chavePixService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        chavePixService.desativar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<ChavePixResponse>> verChaveGerente(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(chavePixService.verChaveGerente(auth, pageable));
    }

    @GetMapping("/minhas")
    public ResponseEntity<Page<ChavePixResponse>> verPropriasChaves(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(chavePixService.verPropriasChaves(auth, pageable));
    }

    @PostMapping("/minhas")
    public ResponseEntity<ChavePixResponse> criarPropriaChave(@RequestBody @Valid ChavePixRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chavePixService.criarPropriaChave(req, auth));
    }

    @PatchMapping("/minhas/{id}")
    public ResponseEntity<ChavePixResponse> atualizarClienteChave(@PathVariable Long id, @RequestBody @Valid ChavePixRequest req, Authentication auth) {
        return ResponseEntity.ok(chavePixService.atualizarClienteChave(id, req, auth));
    }

    @PatchMapping("/minhas/{id}/desativar")
    public ResponseEntity<Void> desativarClienteChave(@PathVariable Long id, Authentication auth) {
        chavePixService.desativarClienteChave(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/minhas/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, Authentication auth) {
        chavePixService.deletar(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
