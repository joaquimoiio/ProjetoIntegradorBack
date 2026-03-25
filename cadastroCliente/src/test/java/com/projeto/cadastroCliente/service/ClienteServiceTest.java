package com.projeto.cadastroCliente.service;

import com.projeto.cadastroCliente.dto.ClienteDto;
import com.projeto.cadastroCliente.dto.PutDTO;
import com.projeto.cadastroCliente.exception.ClienteException;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import com.projeto.cadastroCliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDto clienteFisicaDto;
    private ClienteDto clienteJuridicaDto;
    private Cliente clienteFisica;
    private Cliente clienteJuridica;

    @BeforeEach
    void setUp() {
        // CPF válido: 529.982.247-25
        // CNPJ válido: 04.470.781/0001-39
        clienteFisicaDto = new ClienteDto(
                "João Silva",
                TipoDePessoa.FISICA,
                "52998224725",
                "(11) 99999-9999",
                "joao@email.com"
        );

        clienteJuridicaDto = new ClienteDto(
                "Empresa LTDA",
                TipoDePessoa.JURIDICA,
                "04470781000139",
                "(11) 88888-8888",
                "contato@empresa.com"
        );

        clienteFisica = new Cliente();
        clienteFisica.setId(1L);
        clienteFisica.setNomeDoCliente("João Silva");
        clienteFisica.setTipoDePessoa(TipoDePessoa.FISICA);
        clienteFisica.setCpf("52998224725");
        clienteFisica.setTelefone("(11) 99999-9999");
        clienteFisica.setEmail("joao@email.com");

        clienteJuridica = new Cliente();
        clienteJuridica.setId(2L);
        clienteJuridica.setNomeDoCliente("Empresa LTDA");
        clienteJuridica.setTipoDePessoa(TipoDePessoa.JURIDICA);
        clienteJuridica.setCnpj("04470781000139");
        clienteJuridica.setTelefone("(11) 88888-8888");
        clienteJuridica.setEmail("contato@empresa.com");
    }

    @Test
    void deveCadastrarClienteFisicaComSucesso() {
        when(clienteRepository.existsByCpf(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente resultado = clienteService.cadastrar(clienteFisicaDto);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNomeDoCliente());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveCadastrarClienteJuridicaComSucesso() {
        when(clienteRepository.existsByCnpj(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteJuridica);

        Cliente resultado = clienteService.cadastrar(clienteJuridicaDto);

        assertNotNull(resultado);
        assertEquals("Empresa LTDA", resultado.getNomeDoCliente());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        when(clienteRepository.existsByCpf(anyString())).thenReturn(true);
        assertThrows(ClienteException.class, () -> {
            clienteService.cadastrar(clienteFisicaDto);
        });
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjJaExiste() {
        when(clienteRepository.existsByCnpj(anyString())).thenReturn(true);
        assertThrows(ClienteException.class, () -> {
            clienteService.cadastrar(clienteJuridicaDto);
        });
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        PutDTO putDTO = new PutDTO(
                "(11) 77777-7777",
                "joaao.novo@email.com"
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteFisica));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente resultado = clienteService.atualizar(1L, putDTO);

        assertNotNull(resultado);
        assertEquals("(11) 77777-7777", resultado.getTelefone());
        assertEquals("joao.novo@email.com", resultado.getEmail());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveDeletarCliente() {
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.deletar(1L);

        verify(clienteRepository).deleteById(1L);
    }
}