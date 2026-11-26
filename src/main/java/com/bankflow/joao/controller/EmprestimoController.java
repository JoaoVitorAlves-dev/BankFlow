package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.EmprestimoRequest;
import com.bankflow.joao.dto.response.EmprestimoResponse;
import com.bankflow.joao.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/meus")
    public ResponseEntity<Page<EmprestimoResponse>> listarEmprestimoClienteUsuario(Authentication auth, Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listarEmprestimoClienteUsuario(auth, pageable));
    }

    @PostMapping("/meus")
    public ResponseEntity<EmprestimoResponse> criarEmprestimoClienteUsuario(@RequestBody @Valid EmprestimoRequest req, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.criarEmprestimoClienteUsuario(req, auth));
    }

    @DeleteMapping("/meus/{id}")
    public ResponseEntity<Void> deletarEmprestimoClienteUsuario(@PathVariable Long id, Authentication auth) {
        emprestimoService.deletarEmprestimoClienteUsuario(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/gerente")
    public ResponseEntity<Page<EmprestimoResponse>> listarEmprestimoGerenteClienteUsuario(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(emprestimoService.listarEmprestimoGerenteClienteUsuario(pageable, auth));
    }

    @PatchMapping("/gerente/{id}")
    public ResponseEntity<EmprestimoResponse> atualizarEmprestimoGerenteClienteUsuario(@PathVariable Long id, @RequestBody @Valid EmprestimoRequest req, Authentication auth) {
        return ResponseEntity.ok(emprestimoService.atualizarEmprestimoGerenteClienteUsuario(id, req, auth));
    }

    @GetMapping
    public ResponseEntity<Page<EmprestimoResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> criar(@RequestBody @Valid EmprestimoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmprestimoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EmprestimoRequest req) {
        return ResponseEntity.ok(emprestimoService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
