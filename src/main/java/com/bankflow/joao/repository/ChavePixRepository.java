package com.bankflow.joao.repository;

import com.bankflow.joao.domain.ChavePix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, Long> {
}
