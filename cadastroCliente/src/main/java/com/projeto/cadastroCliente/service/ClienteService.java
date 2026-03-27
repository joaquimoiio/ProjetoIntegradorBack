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

        return clienteRepository.save(cliente);
    }
    public List<Cliente> lista() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id){
        return clienteRepository.findById(id);
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    public Cliente atualizar(Long id, PutDTO putDTO) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteException("Cliente não encontrada"));
        cliente.setEmail(putDTO.email());
        cliente.setTelefone(putDTO.telefone());

        return clienteRepository.save(cliente);
    }

}
