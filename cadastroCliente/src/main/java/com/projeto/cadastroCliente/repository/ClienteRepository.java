package com.projeto.cadastroCliente.repository;

import com.projeto.cadastroCliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);

    List<Cliente> findBySyncFalse();
}