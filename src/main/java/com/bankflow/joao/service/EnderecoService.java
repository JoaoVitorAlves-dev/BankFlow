package com.bankflow.joao.service;

import com.bankflow.joao.domain.Endereco;
import com.bankflow.joao.domain.Usuario;
import com.bankflow.joao.dto.request.EnderecoRequest;
import com.bankflow.joao.dto.response.EnderecoResponse;
import com.bankflow.joao.mapper.EnderecoMapper;
import com.bankflow.joao.repository.EnderecoRepository;
import com.bankflow.joao.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UsuarioRepository usuarioRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioRepository usuarioRepository) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Page<EnderecoResponse> listarTodos(Pageable pageable) {
        return enderecoRepository.findAll(pageable).map(EnderecoMapper::toDTO);
    }

    public Page<EnderecoResponse> listar(Pageable pageable, Authentication auth) {
        return enderecoRepository.findAllByUsuarioEmail(auth.getName(), pageable).map(EnderecoMapper::toDTO);
    }

    public EnderecoResponse criar(EnderecoRequest req) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Endereco entity = EnderecoMapper.toEntity(req, usuario);
        return EnderecoMapper.toDTO(enderecoRepository.save(entity));
    }

    public EnderecoResponse criarProprio(EnderecoRequest req, Authentication auth) {
        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Endereco entity = EnderecoMapper.toEntity(req, usuario);
        return EnderecoMapper.toDTO(enderecoRepository.save(entity));
    }
    
    public EnderecoResponse atualizar(Long id, EnderecoRequest req) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        endereco.setRua(req.rua());
        endereco.setCep(req.cep());
        endereco.setCidade(req.cidade());
        endereco.setEstado(req.estado());

        return EnderecoMapper.toDTO(enderecoRepository.save(endereco));
    }

    public EnderecoResponse atualizarProprio(Long id, EnderecoRequest req, Authentication auth) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!endereco.getUsuario().getEmail().equals(auth.getName()) || !usuario.getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        endereco.setEstado(req.estado());
        endereco.setCidade(req.cidade());
        endereco.setCep(req.cep());
        endereco.setRua(req.rua());

        return EnderecoMapper.toDTO(enderecoRepository.save(endereco));
    }

    public void deletarProprio(Long id, Authentication auth) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!endereco.getUsuario().getEmail().equals(auth.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        enderecoRepository.delete(endereco);
    }

    public void deletar(Long id) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        enderecoRepository.delete(endereco);
    }

}
