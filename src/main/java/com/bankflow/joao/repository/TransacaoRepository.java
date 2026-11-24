package com.bankflow.joao.repository;

import com.bankflow.joao.domain.Transacao;
import com.bankflow.joao.dto.response.TransacaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    Page<Transacao> findAllByOrigemUsuarioEmail(String email, Pageable pageable);

    Page<Transacao> findAllByOrigemAgenciaId(Long id, Pageable pageable);

}
