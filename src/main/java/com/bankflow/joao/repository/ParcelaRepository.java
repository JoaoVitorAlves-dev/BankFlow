package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Parcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    Page<Parcela> findAllByEmprestimoContaUsuarioEmail(String email, Pageable pageable);

    Page<Parcela> findAllByEmprestimoContaAgenciaId(Long id, Pageable pageable);

}
