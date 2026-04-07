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

    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.cpf = :cpf AND c.deletado = false")
    boolean existsByCpfAtivo(@Param("cpf") String cpf);

    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.cnpj = :cnpj AND c.deletado = false")
    boolean existsByCnpjAtivo(@Param("cnpj") String cnpj);

    Cliente findByCpf(String cpf);
    Cliente findByCnpj(String cnpj);
    List<Cliente> findBySync(boolean sync);

    @Query("SELECT c FROM Cliente c WHERE c.deletado = false")
    List<Cliente> findAllClientes();

    @Query("SELECT c FROM Cliente c WHERE c.id = :id AND c.deletado = false")
    Optional<Cliente> findClienteById(@Param("id") Long id);

}