package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.dto.request.AgenciaRequest;
import com.bankflow.joao.dto.response.AgenciaResponse;
import com.bankflow.joao.mapper.AgenciaMapper;
import com.bankflow.joao.repository.AgenciaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;

    public AgenciaService(AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    public Page<AgenciaResponse> listar(Pageable pageable) {
        return agenciaRepository.findAll(pageable).map(AgenciaMapper::toDTO);
    }

    public AgenciaResponse criar(AgenciaRequest req) {
        Agencia entity = AgenciaMapper.toEntity(req);
        return AgenciaMapper.toDTO(agenciaRepository.save(entity));
    }

    public AgenciaResponse atualizar(Long id, AgenciaRequest req) {
        Agencia agencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agencia não encontrado."));

        agencia.setEstado(req.estado());
        agencia.setCodigo(req.codigo());
        agencia.setCidade(req.cidade());
        agencia.setNome(req.nome());

        return AgenciaMapper.toDTO(agenciaRepository.save(agencia));
    }

    public void deletar(Long id) {
        Agencia agencia = agenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agencia não encontrado."));
        agenciaRepository.delete(agencia);
    }

}
