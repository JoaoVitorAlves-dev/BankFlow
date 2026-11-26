package com.bankflow.joao.controller;

import com.bankflow.joao.dto.request.LoginRequest;
import com.bankflow.joao.dto.request.RefreshTokenRequest;
import com.bankflow.joao.dto.request.RegisterRequest;
import com.bankflow.joao.dto.response.RefreshTokenResponse;
import com.bankflow.joao.dto.response.TokenResponse;
import com.bankflow.joao.dto.response.UsuarioResponse;
import com.bankflow.joao.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest req) {
        return ResponseEntity.ok(usuarioService.refresh(req.accessToken()));
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> visualizar(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.visualizar(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> verUsuario(Authentication auth) {
        return ResponseEntity.ok(usuarioService.verUsuario(auth));
    }

    @GetMapping("/agencia")
    public ResponseEntity<Page<UsuarioResponse>> verUsuarioAgencia(Pageable pageable, Authentication auth) {
        return ResponseEntity.ok(usuarioService.verUsuarioAgencia(pageable, auth));
    }

    @PostMapping("/auth/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody @Valid RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrar(req));
    }

    @PostMapping("/auth/logar")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        return ResponseEntity.ok(usuarioService.login(req));
    }

    @PatchMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid RegisterRequest req, Authentication auth) {
        return ResponseEntity.ok(usuarioService.atualizar(id, req, auth));
    }

    @PatchMapping("/usuarios/{id}")
    public ResponseEntity<Void> encerrar(@PathVariable Long id, Authentication auth) {
        usuarioService.encerrar(id, auth);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
