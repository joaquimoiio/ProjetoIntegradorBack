package com.projeto.cadastroCliente.service;

import com.projeto.cadastroCliente.dto.ClienteDto;
import com.projeto.cadastroCliente.dto.PutDTO;
import com.projeto.cadastroCliente.exception.ClienteException;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import com.projeto.cadastroCliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(ClienteDto clienteDto) {

        String documento = clienteDto.cpfCnpj();

        if (clienteDto.tipoDePessoa() == TipoDePessoa.FISICA) {
            if (clienteRepository.existsByCpfAtivo(documento)) {
                throw new ClienteException("CPF já cadastrado: " + documento);
            }
        } else {
            if (clienteRepository.existsByCnpjAtivo(documento)) {
                throw new ClienteException("CNPJ já cadastrado: " + documento);
            }
        }

        if (clienteDto.nomeDoCliente() == null || clienteDto.nomeDoCliente().isBlank()) {
            throw new ClienteException("O nome do cliente não pode estar em branco");
        }

        if (clienteDto.telefone() == null || clienteDto.telefone().isBlank()) {
            throw new ClienteException("O telefone do cliente não pode estar em branco");
        }

        if (clienteDto.tipoDePessoa() == null ) {
            throw new ClienteException("O tipo do cliente não pode estar em branco");
        }
        if (clienteDto.cpfCnpj() == null || clienteDto.cpfCnpj().isEmpty()) {
            throw new ClienteException("O cpf ou cnpj do cliente não pode estar em branco");
        }


        Cliente existente = null;
        if (clienteDto.tipoDePessoa() == TipoDePessoa.FISICA) {
            existente = clienteRepository.findByCpf(documento);
        } else {
            existente = clienteRepository.findByCnpj(documento);
        }

        if (existente != null && existente.isDeletado()) {
            existente.setNomeDoCliente(clienteDto.nomeDoCliente());
            existente.setTipoDePessoa(clienteDto.tipoDePessoa());
            existente.setTelefone(clienteDto.telefone());
            existente.setEmail(clienteDto.email());
            existente.setSync(false);
            existente.setDeletado(false);
            return clienteRepository.save(existente);
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
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteException("Cliente não encontrado"));
        cliente.setSync(false);
        cliente.setDeletado(true);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, PutDTO putDTO) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteException("Cliente não encontrado"));
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
            cliente.setId(null);
            cliente.setSync(true);
            return clienteRepository.save(cliente);

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
