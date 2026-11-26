package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.GerenteRequest;
import com.bankflow.joao.dto.response.GerenteResponse;
import com.bankflow.joao.service.GerenteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gerentes")
public class GerenteController {

    private final GerenteService gerenteService;

    public GerenteController(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
    }

    @GetMapping("/me")
    public ResponseEntity<GerenteResponse> visualizarConta(Authentication auth) {
        return ResponseEntity.ok(gerenteService.visualizarConta(auth));
    }

    @GetMapping
    public ResponseEntity<Page<GerenteResponse>> visualizar(Pageable pageable) {
        return ResponseEntity.ok(gerenteService.visualizar(pageable));
    }

    @PostMapping
    public ResponseEntity<GerenteResponse> criar(@RequestBody @Valid GerenteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenteService.criar(req));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GerenteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid GerenteRequest req) {
        return ResponseEntity.ok(gerenteService.atualizar(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        gerenteService.desativar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        gerenteService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
