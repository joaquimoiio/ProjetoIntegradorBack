package com.projeto.cadastroCliente.service;

import com.projeto.cadastroCliente.dto.ClienteDto;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private ClienteRepository clienteRepository;

    public Cliente cadastrar(ClienteDto clienteDto) {


        Cliente cliente = new Cliente();

        cliente.setNomeDoCliente(clienteDto.nomeDoCliente());
        cliente.setTipoDePessoa(clienteDto.tipoDePessoa());
        cliente.setCpfCnpj(clienteDto.cpfCnpj());
        cliente.setTelefone(clienteDto.telefone());
        cliente.setEmail(clienteDto.email());

        return clienteRepository.save(cliente);
    }


}
