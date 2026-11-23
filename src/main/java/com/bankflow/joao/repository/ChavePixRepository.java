package com.bankflow.joao.repository;

import com.bankflow.joao.domain.ChavePix;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, Long> {

    Page<ChavePix> findAllByContaUsuarioEmail(String email, Pageable pageable);

    Page<ChavePix> findAllByContaAgenciaId(Long id, Pageable pageable);

}
