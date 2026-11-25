package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.AgenciaRequest;
import com.bankflow.joao.dto.response.AgenciaResponse;
import com.bankflow.joao.service.AgenciaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agencias")
public class AgenciaController {

    private final AgenciaService agenciaService;

    public AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    @GetMapping
    public ResponseEntity<Page<AgenciaResponse>> listar(Pageable pageable) {
        return ResponseEntity.ok(agenciaService.listar(pageable));
    }

    @PostMapping
    public ResponseEntity<AgenciaResponse> criar(@RequestBody @Valid AgenciaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agenciaService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AgenciaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid AgenciaRequest req) {
        return ResponseEntity.ok(agenciaService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        agenciaService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
