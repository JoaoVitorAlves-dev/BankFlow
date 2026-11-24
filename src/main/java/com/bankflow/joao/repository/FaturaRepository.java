package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Fatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {

    Page<Fatura> findAllByCartaoContaUsuarioEmail(String email, Pageable pageable);

    Page<Fatura> findAllByCartaoContaAgenciaId(Long id, Pageable pageable);

}
