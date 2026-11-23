package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.domain.Gerente;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.domain.roles.Role;
import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.StatusUsuario;
import com.bankflow.joao.dto.request.ContaRequest;
import com.bankflow.joao.dto.response.ContaResponse;
import com.bankflow.joao.mapper.ContaMapper;
import com.bankflow.joao.repository.AgenciaRepository;
import com.bankflow.joao.repository.ContaRepository;
import com.bankflow.joao.repository.GerenteRepository;
import com.bankflow.joao.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AgenciaRepository agenciaRepository;
    private final GerenteRepository gerenteRepository;

    public ContaService(ContaRepository contaRepository, UsuarioRepository usuarioRepository, AgenciaRepository agenciaRepository, GerenteRepository gerenteRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
        this.agenciaRepository = agenciaRepository;
        this.gerenteRepository = gerenteRepository;
    }

    public Page<ContaResponse> visualizarClientesGerenteAgencia(Authentication auth, Pageable pageable) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado"));
        return contaRepository.findAllByAgenciaId(gerente.getAgencia().getId(), pageable);
    }

    public ContaResponse verConta(Authentication auth) {
        return ContaMapper.toDTO(contaRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrado")));
    }

    public ContaResponse criarGerentePermission(ContaRequest req, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado"));
        Agencia agencia = gerente.getAgencia();

        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario não encontrado"));

        if (!usuario.getStatus().equals(StatusUsuario.ATIVO)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Usuário inativo.");
        }
        if (!usuario.getRole().equals(Role.CLIENTE)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "O usuário informado não possui o perfil de cliente.");
        }

        Conta entity = ContaMapper.toEntity(req, agencia, usuario);

        return ContaMapper.toDTO(contaRepository.save(entity));
    }

    public ContaResponse atualizarGerentePermission(Long id, ContaRequest req, Authentication auth) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrado"));

        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado"));

        if (!conta.getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente não possui acesso a esta agência.");
        }

        if (conta.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A conta não está ativa.");
        }

        if (conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O usuário vinculado à conta não está ativo.");
        }

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente não está ativo.");
        }

        if (conta.getUsuario().getRole() != Role.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A operação é permitida apenas para contas de clientes.");
        }

        conta.setVersion(req.version());
        conta.setTipo(req.tipo());
        conta.setNumero(req.numero());
        conta.setLimite(req.limite());
        conta.setStatus(req.status());

        return ContaMapper.toDTO(contaRepository.save(conta));
    }

    public void deleteById(Long id, Authentication auth) {
        Gerente gerente = gerenteRepository.findByUsuarioEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gerente não encontrado."));

        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != StatusConta.ATIVA ||
                conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.GONE, "A conta ou o usuário vinculado não está ativo.");
        }

        if (conta.getUsuario().getRole() != Role.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A operação é permitida apenas para contas de clientes."
            );
        }

        if (!gerente.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente não está ativo.");
        }

        if (!conta.getAgencia().equals(gerente.getAgencia())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "O gerente não possui permissão para acessar contas de outra agência.");
        }

        contaRepository.delete(conta);
    }

    public Page<ContaResponse> listar(Pageable pageable) {
        return contaRepository.findAll(pageable).map(ContaMapper::toDTO);
    }

    public ContaResponse criar(ContaRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        Agencia agencia = agenciaRepository.findById(req.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agência não encontrada."));

        if (usuario.getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não é possível realizar a operação com um usuário inativo.");
        }

        if (usuario.getRole() != Role.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A operação é permitida apenas para usuários com perfil de cliente.");
        }

        Conta entity = ContaMapper.toEntity(req, agencia, usuario);
        return ContaMapper.toDTO(contaRepository.save(entity));
    }

    public ContaResponse atualizar(Long id, ContaRequest request) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        Agencia agencia = agenciaRepository.findById(request.agenciaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agência não encontrada."));

        if (conta.getStatus() != StatusConta.ATIVA ||
                conta.getUsuario().getStatus() != StatusUsuario.ATIVO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível realizar a operação com uma conta ou usuário inativo.");
        }

        if (conta.getUsuario().getRole() != Role.CLIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A operação é permitida apenas para contas de clientes.");
        }

        conta.setStatus(request.status());
        conta.setLimite(request.limite());
        conta.setNumero(request.numero());
        conta.setTipo(request.tipo());
        conta.setVersion(request.version());
        conta.setAgencia(agencia);

        return ContaMapper.toDTO(contaRepository.save(conta));
    }

    public void deletar(Long id) {
        Conta conta = contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));

        if (conta.getStatus() != StatusConta.ATIVA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível realizar a operação com uma conta inativa.");
        }

        contaRepository.delete(conta);
    }

}
