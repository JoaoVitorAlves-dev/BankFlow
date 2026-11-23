package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Boleto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Long> {

    Page<Boleto> findAllByContaUsuarioEmail(String email, Pageable pageable);

    Page<Boleto> findAllByContaAgenciaId(Long id, Pageable pageable);

}
