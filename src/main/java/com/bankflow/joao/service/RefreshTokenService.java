package com.bankflow.joao.service;

import com.bankflow.joao.domain.RefreshToken;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long expiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken criarERotacionar(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);

        RefreshToken rt = new RefreshToken();
        rt.setExpiraEm(Instant.now().plus(expiration, ChronoUnit.DAYS));
        rt.setUsuario(usuario);
        rt.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(rt);
    }

    @Transactional
    public RefreshToken validarToken(String token) {
        RefreshToken atual = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (atual.getExpiraEm().isBefore(Instant.now())) {
            refreshTokenRepository.delete(atual);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return criarERotacionar(atual.getUsuario());
    }

    @Transactional
    public void deletarPorToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
    }

}
