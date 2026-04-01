package com.projeto.cadastroCliente.model;

import com.projeto.cadastroCliente.model.enums.TipoDePessoa;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeDoCliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDePessoa tipoDePessoa;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String telefone;

    private String email;

    private boolean sync = false;

    private boolean deletado = false;

    public boolean isDeletado() {
        return deletado;
    }

    public void setDeletado(boolean deletado) {
        this.deletado = deletado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeDoCliente() {
        return nomeDoCliente;
    }

    public void setNomeDoCliente(String nomeDoCliente) {
        this.nomeDoCliente = nomeDoCliente;
    }

    public TipoDePessoa getTipoDePessoa() {
        return tipoDePessoa;
    }

    public void setTipoDePessoa(TipoDePessoa tipoDePessoa) {
        this.tipoDePessoa = tipoDePessoa;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    @PrePersist
    @PreUpdate
    private void validarDocumento() {
        if (tipoDePessoa == TipoDePessoa.FISICA) {
            if (cpf == null || cpf.isBlank()) {
                throw new IllegalStateException("CPF é obrigatório para pessoa física");
            }
            if (cnpj != null && !cnpj.isBlank()) {
                throw new IllegalStateException("Pessoa física não pode ter CNPJ");
            }
        } else if (tipoDePessoa == TipoDePessoa.JURIDICA) {
            if (cnpj == null || cnpj.isBlank()) {
                throw new IllegalStateException("CNPJ é obrigatório para pessoa jurídica");
            }
            if (cpf != null && !cpf.isBlank()) {
                throw new IllegalStateException("Pessoa jurídica não pode ter CPF");
            }
        }
    }
}