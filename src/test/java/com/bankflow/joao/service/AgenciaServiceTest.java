package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.dto.request.AgenciaRequest;
import com.bankflow.joao.dto.response.AgenciaResponse;
import com.bankflow.joao.repository.AgenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AgenciaServiceTest {

    @Mock
    private AgenciaRepository agenciaRepository;

    @InjectMocks
    private AgenciaService agenciaService;

    @Captor
    private ArgumentCaptor<Agencia> agenciaCaptor;

    @Test
    void listarAgencias() {
    }

    @Test
    void criarAgencia() {
        AgenciaRequest request = new AgenciaRequest("89520-000", "Agencia ZERO", "Balneario", "Santa Catarina");

        when(agenciaRepository.save(any(Agencia.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        agenciaService.criar(request);

        verify(agenciaRepository).save(agenciaCaptor.capture());
        Agencia salva = agenciaCaptor.getValue();

        assertEquals("Agencia ZERO", salva.getNome());
        assertEquals("Balneario", salva.getCidade());
        assertEquals("89520-000", salva.getCodigo());
        assertEquals("Santa Catarina", salva.getEstado());
    }

    @Test
    void atualizarAgencia() {
        Agencia existente = new Agencia();
        existente.setNome("Agencia Antiga");

        AgenciaRequest agencia = new AgenciaRequest("1021102-121", "Agencia Nova", "Angra", "Rio de Janeiro");

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(agenciaRepository.save(any(Agencia.class))).thenAnswer(inv -> inv.getArgument(0));

        agenciaService.atualizar(1L, agencia);

        verify(agenciaRepository).save(agenciaCaptor.capture());
        Agencia salva = agenciaCaptor.getValue();

        assertEquals("Agencia Nova", salva.getNome());
        assertEquals("Angra", salva.getCidade());
    }

    @Test
    void atualizarAgencia_inexistente() {
        AgenciaRequest agencia = new AgenciaRequest("1021102-121", "Agencia nova", "Angra", "Rio de Janeiro");

        when(agenciaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> agenciaService.atualizar(99L, agencia));

        verify(agenciaRepository, never()).save(any());
    }

    @Test
    void deletarAgencia() {
        Agencia agencia = new Agencia();
        agencia.setNome("Agencia");

        when(agenciaRepository.findById(1L)).thenReturn(Optional.of(agencia));

        agenciaService.deletar(1L);

        verify(agenciaRepository).delete(agencia);
    }

}
