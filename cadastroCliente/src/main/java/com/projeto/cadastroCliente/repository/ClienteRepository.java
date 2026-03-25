package com.projeto.cadastroCliente.repository;

import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.model.enums.CpfCnpj;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpfCnpj(CpfCnpj cpfCnpj);

}