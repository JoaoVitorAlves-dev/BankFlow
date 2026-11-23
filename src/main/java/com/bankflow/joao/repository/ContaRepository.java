package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.dto.response.ContaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    ContaResponse findByUsuarioEmail(String email);

    Page<ContaResponse> findAllByAgenciaId(Long id, Pageable pageable);

}
