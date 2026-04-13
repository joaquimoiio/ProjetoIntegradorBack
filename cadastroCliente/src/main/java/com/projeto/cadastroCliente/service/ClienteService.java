package com.projeto.cadastroCliente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.cadastroCliente.dto.ClienteDto;
import com.projeto.cadastroCliente.dto.PutDTO;
import com.projeto.cadastroCliente.exception.ClienteException;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import com.projeto.cadastroCliente.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente cadastrar(ClienteDto clienteDto) {

        String documento = clienteDto.cpfCnpj();

        if (clienteDto.nomeDoCliente() == null || clienteDto.nomeDoCliente().isBlank()) {
            throw new ClienteException("O nome do cliente não pode estar em branco");
        }

        if (clienteDto.telefone() == null || clienteDto.telefone().isBlank()) {
            throw new ClienteException("O telefone do cliente não pode estar em branco");
        }

        String telefoneLimpo = clienteDto.telefone().replaceAll("\\D", "");
        if (telefoneLimpo.length() != 11) {
            throw new ClienteException("Telefone inválido: deve conter 11 dígitos");
        }
        if (clienteDto.tipoDePessoa() == null) {
            throw new ClienteException("O tipo do cliente não pode estar em branco");
        }
        if (clienteDto.cpfCnpj() == null || clienteDto.cpfCnpj().isEmpty()) {
            throw new ClienteException("O cpf ou cnpj do cliente não pode estar em branco");
        }

        if (clienteDto.tipoDePessoa() == TipoDePessoa.FISICA) {
            if (!validarCPF(documento)) {
                throw new ClienteException("CPF inválido: " + documento);
            }
        } else {
            if (!validarCNPJ(documento)) {
                throw new ClienteException("CNPJ inválido: " + documento);
            }
        }

        documento = documento.replaceAll("\\D", "");

        if (clienteDto.tipoDePessoa() == TipoDePessoa.FISICA) {
            if (clienteRepository.existsByCpfAtivo(documento)) {
                throw new ClienteException("CPF já cadastrado: " + documento);
            }
        } else {
            if (clienteRepository.existsByCnpjAtivo(documento)) {
                throw new ClienteException("CNPJ já cadastrado: " + documento);
            }
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

    public Optional<Cliente> buscarPorId(Long id) {
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

        if (cliente.getNomeDoCliente() == null || cliente.getNomeDoCliente().isBlank()) {
            throw new ClienteException("O nome do cliente não pode estar em branco");
        }

        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new ClienteException("O telefone do cliente não pode estar em branco");
        }

        String telefoneLimpo = cliente.getTelefone().replaceAll("\\D", "");
        if (telefoneLimpo.length() != 11) {
            throw new ClienteException("Telefone inválido: deve conter 11 dígitos");
        }

        if (cliente.getTipoDePessoa() == null) {
            throw new ClienteException("O tipo do cliente não pode estar em branco");
        }

        String cpf = (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) ? cliente.getCpf() : null;
        String cnpj = (cliente.getCnpj() != null && !cliente.getCnpj().isEmpty()) ? cliente.getCnpj() : null;

        if (cpf == null && cnpj == null) {
            throw new ClienteException("O cpf ou cnpj do cliente não pode estar em branco");
        }

        if (cpf != null) {
            if (!validarCPF(cpf)) {
                throw new ClienteException("CPF inválido: " + cpf);
            }
            cpf = cpf.replaceAll("\\D", "");
        }

        if (cnpj != null) {
            if (!validarCNPJ(cnpj)) {
                throw new ClienteException("CNPJ inválido: " + cnpj);
            }
            cnpj = cnpj.replaceAll("\\D", "");
        }

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

    public void marcarClienteComSync(List<Cliente> clientes) {
        for (Cliente cliente : clientes) {
            cliente.setSync(true);
            clienteRepository.save(cliente);
        }
    }

    private boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            return (cpf.charAt(9) - '0' == primeiroDigito) &&
                    (cpf.charAt(10) - '0' == segundoDigito);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * peso1[i];
            }
            int primeiroDigito = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * peso2[i];
            }
            int segundoDigito = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            return (cnpj.charAt(12) - '0' == primeiroDigito) &&
                    (cnpj.charAt(13) - '0' == segundoDigito);
        } catch (Exception e) {
            return false;
        }
    }
}