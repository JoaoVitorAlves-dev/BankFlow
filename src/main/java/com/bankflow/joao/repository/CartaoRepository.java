package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Cartao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    Page<Cartao> findAllByContaUsuarioEmail(String email, Pageable pageable);

    Page<Cartao> findAllByContaAgenciaId(Long id, Pageable pageable);

}
