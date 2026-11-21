package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.Bandeira;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "tb_cartao")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Column(nullable = false)
    private String ultimos4;

    @Enumerated(EnumType.STRING)
    private Bandeira bandeira;

    @Column(nullable = false)
    private YearMonth validade;

    @Column(nullable = false)
    private BigDecimal limite = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Cartao() {
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

    public String getUltimos4() {
        return ultimos4;
    }

    public void setUltimos4(String ultimos4) {
        this.ultimos4 = ultimos4;
    }

    public Bandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(Bandeira bandeira) {
        this.bandeira = bandeira;
    }

    public YearMonth getValidade() {
        return validade;
    }

    public void setValidade(YearMonth validade) {
        this.validade = validade;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
