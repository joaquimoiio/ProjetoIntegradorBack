package com.projeto.cadastroCliente.repository;

import com.projeto.cadastroCliente.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);

    Cliente findByCpf(String cpf);
    Cliente findByCnpj(String cnpj);
    List<Cliente> findBySync(boolean sync);

    @Query("SELECT c FROM Cliente c WHERE c.deletado = false")
    List<Cliente> findAllClientes();

    @Query("SELECT c FROM Cliente c WHERE c.id = :id AND c.deletado = false")
    Optional<Cliente> findClienteById(@Param("id") Long id);

}