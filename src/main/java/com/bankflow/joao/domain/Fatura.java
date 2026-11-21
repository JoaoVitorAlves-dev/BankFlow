package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.StatusFatura;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "tb_faturas")
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @Column(nullable = false, name = "mes_ref")
    private YearMonth mesRef;

    @Column(nullable = false, name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate vencimento;

    @Enumerated(EnumType.STRING)
    private StatusFatura status;

    public Fatura() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public YearMonth getMesRef() {
        return mesRef;
    }

    public void setMesRef(YearMonth mesRef) {
        this.mesRef = mesRef;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public StatusFatura getStatus() {
        return status;
    }

    public void setStatus(StatusFatura status) {
        this.status = status;
    }
}
