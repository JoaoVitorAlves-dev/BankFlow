package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.TipoLancamento;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_lancamentos")
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transacao_id", nullable = false)
    private Transacao transacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    private TipoLancamento tipo;

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false, name = "saldo_apos")
    private BigDecimal saldoApos = BigDecimal.ZERO;

    public Lancamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transacao getTransacao() {
        return transacao;
    }

    public void setTransacao(Transacao transacao) {
        this.transacao = transacao;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoLancamento tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getSaldoApos() {
        return saldoApos;
    }

    public void setSaldoApos(BigDecimal saldoApos) {
        this.saldoApos = saldoApos;
    }
}
