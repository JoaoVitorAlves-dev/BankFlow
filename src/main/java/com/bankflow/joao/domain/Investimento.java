package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.TipoInvestimento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_investimentos")
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    private TipoInvestimento tipo;

    @Column(nullable = false, name = "valor_aplicado")
    private BigDecimal valorAplicado = BigDecimal.ZERO;

    @Column(nullable = false, name = "taxa_anual")
    private BigDecimal taxaAnual = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate vencimento;

    public Investimento() {
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

    public TipoInvestimento getTipo() {
        return tipo;
    }

    public void setTipo(TipoInvestimento tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValorAplicado() {
        return valorAplicado;
    }

    public void setValorAplicado(BigDecimal valorAplicado) {
        this.valorAplicado = valorAplicado;
    }

    public BigDecimal getTaxaAnual() {
        return taxaAnual;
    }

    public void setTaxaAnual(BigDecimal taxaAnual) {
        this.taxaAnual = taxaAnual;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }
}
