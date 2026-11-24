package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.StatusTransacao;
import com.bankflow.joao.domain.roles.TipoTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_transacaos")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "origem_id")
    private Conta origem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destino_id")
    private Conta destino;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status = StatusTransacao.PENDENTE;

    @Column(nullable = false, name = "chave_destino")
    private String chaveDestino;

    @Column(nullable = false, name = "idempotency_key")
    private String idempotencykey;

    @Column(nullable = false, name = "criada_em")
    private Instant criadaEm;

    @PrePersist
    public void criada() {
        this.criadaEm = Instant.now();
    }

    public Transacao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conta getOrigem() {
        return origem;
    }

    public void setOrigem(Conta origem) {
        this.origem = origem;
    }

    public Conta getDestino() {
        return destino;
    }

    public void setDestino(Conta destino) {
        this.destino = destino;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusTransacao getStatus() {
        return status;
    }

    public void setStatus(StatusTransacao status) {
        this.status = status;
    }

    public String getChaveDestino() {
        return chaveDestino;
    }

    public void setChaveDestino(String chaveDestino) {
        this.chaveDestino = chaveDestino;
    }

    public String getIdempotencykey() {
        return idempotencykey;
    }

    public void setIdempotencykey(String idempotencykey) {
        this.idempotencykey = idempotencykey;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(Instant criadaEm) {
        this.criadaEm = criadaEm;
    }
}
