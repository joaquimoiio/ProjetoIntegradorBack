package com.projeto.cadastroCliente.model;


import com.projeto.cadastroCliente.model.enums.CpfCnpj;
import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeDoCliente;

    @Column(nullable = false)
    private TipoDePessoa tipoDePessoa;

    @Column(nullable = false, unique = true)
    private CpfCnpj cpfCnpj;

    @Column(nullable = false)
    private String telefone;

    private String email;


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

    public CpfCnpj getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(CpfCnpj cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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
}
