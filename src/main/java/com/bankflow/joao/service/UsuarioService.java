package com.bankflow.joao.service;

import com.bankflow.joao.domain.RefreshToken;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.domain.roles.Role;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.LoginRequest;
import com.bankflow.joao.dto.request.RegisterRequest;
import com.bankflow.joao.dto.response.RefreshTokenResponse;
import com.bankflow.joao.dto.response.TokenResponse;
import com.bankflow.joao.dto.response.UsuarioResponse;
import com.bankflow.joao.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RefreshTokenService refreshTokenService, TokenService tokenService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.refreshTokenService = refreshTokenService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public RefreshTokenResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenService.validarERotacionar(token);

        String access = tokenService.gerarToken(refreshToken.getToken());

        return new RefreshTokenResponse(access, refreshToken.getToken());
    }

//    public Page<UsuarioResponse> visualizar(Pageable pageable) {
//        usuarioRepository.findAll(pageable)
//    }

    @Transactional
    public UsuarioResponse registrar(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(req.nome());
        usuario.setCpf(req.cpf());
        usuario.setEmail(req.email());
        usuario.setPassword(passwordEncoder.encode(req.password()));
        usuario.setRole(Role.CLIENTE);
        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getStatus());
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = tokenService.gerarToken(request.email());
        return new TokenResponse(token);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, RegisterRequest req, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (usuario.getStatus() == StatusUsuario.BLOQUEADO || usuario.getStatus() == StatusUsuario.ENCERRADO) {
            throw new ResponseStatusException(HttpStatus.GONE, "Não é possível atualizar um usuário bloqueado ou encerrado.");
        }
        if (!usuario.getEmail().equals(auth.getName()) || !req.email().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar este usuário ou alterar o e-mail para outro endereço.");
        }

        usuario.setNome(req.nome());
        usuario.setEmail(req.email());
        usuario.setCpf(req.cpf());
        usuario.setPassword(passwordEncoder.encode(req.password()));
        usuarioRepository.save(usuario);

        return new UsuarioResponse(usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getStatus());
    }

    @Transactional
    public void encerrar(Long id, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        if (usuario.getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.GONE, "Apenas usuários ativos podem encerrar a conta.");
        }
        if (!usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para encerrar esta conta.");
        }

        usuario.setStatus(StatusUsuario.ENCERRADO);
        usuarioRepository.save(usuario);
    }

}