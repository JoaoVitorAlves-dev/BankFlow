package com.bankflow.joao.service;

import com.bankflow.joao.domain.Agencia;
import com.bankflow.joao.dto.request.AgenciaRequest;
import com.bankflow.joao.repository.AgenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

}
