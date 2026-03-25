package com.projeto.cadastroCliente.repository;

import com.projeto.cadastroCliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
}