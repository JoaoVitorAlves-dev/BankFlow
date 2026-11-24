package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Emprestimo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    Page<Emprestimo> findAllByContaUsuarioEmail(String email, Pageable pageable);

    Page<Emprestimo> findAllByContaAgenciaId(Long id, Pageable pageable);

}
