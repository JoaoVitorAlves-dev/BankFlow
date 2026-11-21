package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.StatusParcela;
import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_parcelas")
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emprestimo_id", nullable = false)
    private Emprestimo emprestimo;

    @Column(nullable = false)
    private Integer numero = 0;

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate vencimento;

    @Enumerated(EnumType.STRING)
    private StatusParcela status;

    public Parcela() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public StatusParcela getStatus() {
        return status;
    }

    public void setStatus(StatusParcela status) {
        this.status = status;
    }
}
