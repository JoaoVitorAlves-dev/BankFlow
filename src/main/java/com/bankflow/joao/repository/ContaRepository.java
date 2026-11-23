package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Conta;
import com.bankflow.joao.dto.response.ContaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByUsuarioEmail(String email);

    Page<ContaResponse> findAllByAgenciaId(Long id, Pageable pageable);

}
