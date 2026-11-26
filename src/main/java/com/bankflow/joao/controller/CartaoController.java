package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.CartaoRequest;
import com.bankflow.joao.dto.response.CartaoResponse;
import com.bankflow.joao.service.CartaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartaos")
public class CartaoController {

    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<CartaoResponse>> listarCartaoGerente(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(cartaoService.listarCartaoGerente(auth, pageable));
    }

    @PostMapping("/gerente")
    public ResponseEntity<CartaoResponse> criarCartaoGerente(@RequestBody @Valid CartaoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoService.criarCartaoGerente(req, auth));
    }

    @PatchMapping("/gerente/{id}")
    public ResponseEntity<CartaoResponse> atualizarCartaoGerente(@PathVariable Long id, @RequestBody @Valid CartaoRequest req, Authentication auth) {
        return ResponseEntity.ok(cartaoService.atualizarCartaoGerente(id, req, auth));
    }

    @PatchMapping("/gerente/{id}/status")
    public ResponseEntity<Void> desativarCartaoGerente(@PathVariable Long id, Authentication auth) {
        cartaoService.desativarCartaoGerente(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/gerente/{id}")
    public ResponseEntity<Void> deletarCartaoGerente(@PathVariable Long id, Authentication auth) {
        cartaoService.deletarCartaoGerente(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/me")
    public ResponseEntity<Page<CartaoResponse>> listarCartaoCLiente(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(cartaoService.listarCartaoCliente(auth, pageable));
    }

    @PatchMapping("/me/{id}")
    public ResponseEntity<CartaoResponse> atualizarProprioCartao(@PathVariable Long id, @RequestBody @Valid CartaoRequest req, Authentication auth) {
        return ResponseEntity.ok(cartaoService.atualizarProprioCartao(id, req, auth));
    }

    @GetMapping
    public ResponseEntity<Page<CartaoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(cartaoService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<CartaoResponse> criar(@RequestBody @Valid CartaoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartaoService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CartaoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid CartaoRequest req) {
        return ResponseEntity.ok(cartaoService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cartaoService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        cartaoService.desativar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
