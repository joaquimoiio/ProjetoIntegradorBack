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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
        when(clienteRepository.existsByCpfAtivo(anyString())).thenReturn(false);
        when(clienteRepository.findByCpf(anyString())).thenReturn(null);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente resultado = clienteService.cadastrar(clienteFisicaDto);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNomeDoCliente());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveCadastrarClienteJuridicaComSucesso() {
        when(clienteRepository.existsByCnpjAtivo(anyString())).thenReturn(false);
        when(clienteRepository.findByCnpj(anyString())).thenReturn(null);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteJuridica);

        Cliente resultado = clienteService.cadastrar(clienteJuridicaDto);

        assertNotNull(resultado);
        assertEquals("Empresa LTDA", resultado.getNomeDoCliente());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfAtivoJaExiste() {
        when(clienteRepository.existsByCpfAtivo(anyString())).thenReturn(true);

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(clienteFisicaDto));

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjAtivoJaExiste() {
        when(clienteRepository.existsByCnpjAtivo(anyString())).thenReturn(true);

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(clienteJuridicaDto));

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveReativarClienteFisicaDeletado() {
        clienteFisica.setDeletado(true);

        when(clienteRepository.existsByCpfAtivo(anyString())).thenReturn(false);
        when(clienteRepository.findByCpf(anyString())).thenReturn(clienteFisica);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente resultado = clienteService.cadastrar(clienteFisicaDto);

        assertNotNull(resultado);
        assertFalse(clienteFisica.isDeletado());
        assertFalse(clienteFisica.isSync());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeEmBrancoCadastrar() {
        ClienteDto dto = new ClienteDto(
                "", TipoDePessoa.FISICA, "52998224725", "(11) 99999-9999", "email@email.com"
        );

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(dto));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneEmBrancoCadastrar() {
        ClienteDto dto = new ClienteDto(
                "João Silva", TipoDePessoa.FISICA, "52998224725", "", "email@email.com"
        );

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(dto));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneInvalidoCadastrar() {
        ClienteDto dto = new ClienteDto(
                "João Silva", TipoDePessoa.FISICA, "52998224725", "123", "email@email.com"
        );

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(dto));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfInvalidoCadastrar() {
        ClienteDto dto = new ClienteDto(
                "João Silva", TipoDePessoa.FISICA, "12345678900", "(11) 99999-9999", "email@email.com"
        );

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(dto));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjInvalidoCadastrar() {
        ClienteDto dto = new ClienteDto(
                "Empresa LTDA", TipoDePessoa.JURIDICA, "11111111111111", "(11) 88888-8888", "email@email.com"
        );

        assertThrows(ClienteException.class, () -> clienteService.cadastrar(dto));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveAtualizarClienteComSucesso() {
        PutDTO putDTO = new PutDTO("(11) 77777-7777", "joao.novo@email.com");

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setId(1L);
        clienteAtualizado.setNomeDoCliente("João Silva");
        clienteAtualizado.setTipoDePessoa(TipoDePessoa.FISICA);
        clienteAtualizado.setCpf("52998224725");
        clienteAtualizado.setTelefone("(11) 77777-7777");
        clienteAtualizado.setEmail("joao.novo@email.com");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteFisica));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtualizado);

        Cliente resultado = clienteService.atualizar(1L, putDTO);

        assertNotNull(resultado);
        assertEquals("(11) 77777-7777", resultado.getTelefone());
        assertEquals("joao.novo@email.com", resultado.getEmail());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        PutDTO putDTO = new PutDTO("(11) 77777-7777", "joao.novo@email.com");

        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteException.class, () -> clienteService.atualizar(99L, putDTO));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveDeletarClienteMudandoEstadoDeDelete() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteFisica));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente resultado = clienteService.deletar(1L);

        assertNotNull(resultado);
        assertTrue(clienteFisica.isDeletado());
        assertFalse(clienteFisica.isSync());
        verify(clienteRepository).save(any(Cliente.class));
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void deveLancarExcecaoAoDeletarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ClienteException.class, () -> clienteService.deletar(99L));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }


    @Test
    void deveSalvarClienteNovoComCpf() {
        clienteFisica.setSync(true);

        when(clienteRepository.existsByCpf(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente resultado = clienteService.salvar(clienteFisica);

        assertNotNull(resultado);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveSalvarClienteNovoComCnpj() {
        clienteJuridica.setSync(true);

        when(clienteRepository.existsByCnpj(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteJuridica);

        Cliente resultado = clienteService.salvar(clienteJuridica);

        assertNotNull(resultado);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveAtualizarClienteExistenteComCpfNoSalvar() {
        when(clienteRepository.existsByCpf("52998224725")).thenReturn(true);
        when(clienteRepository.findByCpf("52998224725")).thenReturn(clienteFisica);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteFisica);

        Cliente novo = new Cliente();
        novo.setNomeDoCliente("João Atualizado");
        novo.setTipoDePessoa(TipoDePessoa.FISICA);
        novo.setCpf("52998224725");
        novo.setTelefone("(11) 99999-9999");
        novo.setEmail("novo@email.com");

        Cliente resultado = clienteService.salvar(novo);

        assertNotNull(resultado);
        assertEquals("João Atualizado", clienteFisica.getNomeDoCliente());
        assertTrue(clienteFisica.isSync());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeEmBrancoSalvar() {
        Cliente cliente = new Cliente();
        cliente.setNomeDoCliente("");
        cliente.setTipoDePessoa(TipoDePessoa.FISICA);
        cliente.setCpf("52998224725");
        cliente.setTelefone("(11) 99999-9999");

        assertThrows(ClienteException.class, () -> clienteService.salvar(cliente));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneInvalidoSalvar() {
        Cliente cliente = new Cliente();
        cliente.setNomeDoCliente("João Silva");
        cliente.setTipoDePessoa(TipoDePessoa.FISICA);
        cliente.setCpf("52998224725");
        cliente.setTelefone("123");

        assertThrows(ClienteException.class, () -> clienteService.salvar(cliente));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfInvalidoSalvar() {
        Cliente cliente = new Cliente();
        cliente.setNomeDoCliente("João Silva");
        cliente.setTipoDePessoa(TipoDePessoa.FISICA);
        cliente.setCpf("12345678900");
        cliente.setTelefone("(11) 99999-9999");

        assertThrows(ClienteException.class, () -> clienteService.salvar(cliente));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoSemCpfECnpjSalvar() {
        Cliente cliente = new Cliente();
        cliente.setNomeDoCliente("João Silva");
        cliente.setTipoDePessoa(TipoDePessoa.FISICA);
        cliente.setTelefone("(11) 99999-9999");

        assertThrows(ClienteException.class, () -> clienteService.salvar(cliente));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveListarClientesNaoSincronizados() {
        when(clienteRepository.findBySync(false)).thenReturn(List.of(clienteFisica));

        List<Cliente> resultado = clienteService.listaNaoSincronizados();

        assertEquals(1, resultado.size());
        verify(clienteRepository).findBySync(false);
    }

    @Test
    void deveMarcarClientesComoSincronizados() {
        List<Cliente> clientes = List.of(clienteFisica, clienteJuridica);

        clienteService.marcarClienteComSync(clientes);

        assertTrue(clienteFisica.isSync());
        assertTrue(clienteJuridica.isSync());
        verify(clienteRepository, times(2)).save(any(Cliente.class));
    }
}