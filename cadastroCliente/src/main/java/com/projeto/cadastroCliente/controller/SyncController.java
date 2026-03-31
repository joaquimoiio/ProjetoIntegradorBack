package com.projeto.cadastroCliente.controller;

import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cliente")
    public ResponseEntity<Cliente> receberCliente(@RequestBody Cliente cliente) {
        Cliente salvo = clienteService.salvar(cliente);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/cliente/exists/{cpf}/{cnpj}")
    public ResponseEntity<Boolean> existeCliente(
            @PathVariable(required = false) String cpf,
            @PathVariable(required = false) String cnpj) {
        boolean exists = clienteService.existeCliente(cpf, cnpj);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = clienteService.listaNaoSincronizados();
        clienteService.marcarClienteComSync(clientes);
        return ResponseEntity.ok(clientes);
    }
}