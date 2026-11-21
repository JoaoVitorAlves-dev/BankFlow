package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.StatusEmprestimo;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal taxa_juros = BigDecimal.ZERO;

    @Column(nullable = false, name = "qtd_parcelas")
    private Integer qtdParcelas = 0;

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo statusEmprestimo;

    public Emprestimo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getTaxa_juros() {
        return taxa_juros;
    }

    public void setTaxa_juros(BigDecimal taxa_juros) {
        this.taxa_juros = taxa_juros;
    }

    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public StatusEmprestimo getStatusEmprestimo() {
        return statusEmprestimo;
    }

    public void setStatusEmprestimo(StatusEmprestimo statusEmprestimo) {
        this.statusEmprestimo = statusEmprestimo;
    }
}
