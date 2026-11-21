package com.bankflow.joao.domain;

import com.bankflow.joao.domain.roles.TipoChave;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "tb_chave_pix")
public class ChavePix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @Enumerated(EnumType.STRING)
    private TipoChave tipo;

    @Column(nullable = false, unique = true)
    private String valor;

    @Column(nullable = false)
    private Boolean ativa = true;

    @Column(nullable = false, name = "criada_em")
    private Instant criadaEm;

    public ChavePix() {
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

    public TipoChave getTipo() {
        return tipo;
    }

    public void setTipo(TipoChave tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(Instant criadaEm) {
        this.criadaEm = criadaEm;
    }
}
