package com.projeto.cadastroCliente.service;

import com.projeto.cadastroCliente.dto.ClienteDto;
import com.projeto.cadastroCliente.dto.PutDTO;
import com.projeto.cadastroCliente.exception.ClienteException;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import com.projeto.cadastroCliente.repository.ClienteRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(ClienteDto clienteDto) {

        String documento = clienteDto.cpfCnpj();

        if (clienteDto.tipoDePessoa() == TipoDePessoa.FISICA) {
            if (clienteRepository.existsByCpf(documento)) {
                throw new ClienteException("CPF já cadastrado: " + documento);
            }
        } else {
            if (clienteRepository.existsByCnpj(documento)) {
                throw new ClienteException("CNPJ já cadastrado: " + documento);
            }
        }

          Cliente cliente = new Cliente();

        cliente.setNomeDoCliente(clienteDto.nomeDoCliente());
        cliente.setTipoDePessoa(clienteDto.tipoDePessoa());
        if (clienteDto.tipoDePessoa() == TipoDePessoa.FISICA) {
            cliente.setCpf(documento);
            cliente.setCnpj(null);
        } else {
            cliente.setCnpj(documento);
            cliente.setCpf(null);
        }
        cliente.setTelefone(clienteDto.telefone());
        cliente.setEmail(clienteDto.email());
        cliente.setSync(false);
        cliente.setDeletado(false);

        return clienteRepository.save(cliente);
    }
    public List<Cliente> lista() {
        return clienteRepository.findAllClientes();
    }

    public Optional<Cliente> buscarPorId(Long id){
        return clienteRepository.findClienteById(id);
    }

    public Cliente deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteException("Cliente não encontrada"));
        cliente.setSync(false);
        cliente.setDeletado(true);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, PutDTO putDTO) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteException("Cliente não encontrada"));
        cliente.setEmail(putDTO.email());
        cliente.setTelefone(putDTO.telefone());
        cliente.setSync(false);

        return clienteRepository.save(cliente);
    }

    public Cliente salvar(Cliente cliente) {

            String cpf = (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) ? cliente.getCpf() : null;
            String cnpj = (cliente.getCnpj() != null && !cliente.getCnpj().isEmpty()) ? cliente.getCnpj() : null;

            cliente.setCpf(cpf);
            cliente.setCnpj(cnpj);

            if (cpf != null && clienteRepository.existsByCpf(cpf)) {
                Cliente existente = clienteRepository.findByCpf(cpf);
                existente.setNomeDoCliente(cliente.getNomeDoCliente());
                existente.setTelefone(cliente.getTelefone());
                existente.setEmail(cliente.getEmail());
                existente.setSync(true);
                existente.setDeletado(cliente.isDeletado());
                return clienteRepository.save(existente);
            }

            if (cnpj != null && clienteRepository.existsByCnpj(cnpj)) {
                Cliente existente = clienteRepository.findByCnpj(cnpj);
                existente.setNomeDoCliente(cliente.getNomeDoCliente());
                existente.setTelefone(cliente.getTelefone());
                existente.setEmail(cliente.getEmail());
                existente.setSync(true);
                existente.setDeletado(cliente.isDeletado());
                return clienteRepository.save(existente);
            }

            cliente.setSync(true);
            return clienteRepository.save(cliente);

    }

    public boolean existeCliente(String cpf, String cnpj) {
        if (cpf != null && !cpf.equals("null")) {
            return clienteRepository.existsByCpf(cpf);
        }
        if (cnpj != null && !cnpj.equals("null")) {
            return clienteRepository.existsByCnpj(cnpj);
        }
        return false;
    }

    public List<Cliente> listaNaoSincronizados() {
        return clienteRepository.findBySync(false);
    }

    public void marcarClienteComSync(List<Cliente> clientes){
        for (Cliente cliente : clientes) {
            cliente.setSync(true);
            clienteRepository.save(cliente);
        }
    }
}
