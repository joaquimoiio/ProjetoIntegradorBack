package com.projeto.cadastroCliente.controller;

import com.projeto.cadastroCliente.dto.ClienteDto;
import com.projeto.cadastroCliente.dto.PutDTO;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrarCliente(@Valid @RequestBody ClienteDto clienteDto) {
        return ResponseEntity.ok(clienteService.cadastrar(clienteDto));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodosClientes() {
        return ResponseEntity.ok(clienteService.lista());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Cliente>> buscarClientePorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody PutDTO putDTO) {
        return ResponseEntity.ok(clienteService.atualizar(id, putDTO));
    }

    @DeleteMapping("/{id}")
    public void deletarCliente(@PathVariable Long id) {
        clienteService.deletar(id);
    }
}