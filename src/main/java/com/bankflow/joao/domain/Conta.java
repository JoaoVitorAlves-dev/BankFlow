package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.StatusConta;
import com.bankflow.joao.domain.roles.TipoConta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "tb_conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agencia_id")
    private Agencia agencia;

    @Column(nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal limite = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private StatusConta status = StatusConta.ATIVA;

    @Column(nullable = false)
    private Long version;

    @Column(nullable = false, name = "criada_em")
    private Instant criadaEm;

    @PrePersist
    public void criada() {
        this.criadaEm = Instant.now();
    }

    public Conta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public void setAgencia(Agencia agencia) {
        this.agencia = agencia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    public StatusConta getStatus() {
        return status;
    }

    public void setStatus(StatusConta status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(Instant criadaEm) {
        this.criadaEm = criadaEm;
    }
}
