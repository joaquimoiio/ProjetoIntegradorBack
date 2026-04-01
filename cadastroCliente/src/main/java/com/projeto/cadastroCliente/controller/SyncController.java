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
    private ClienteService service;

    @PostMapping("/cliente")
    public ResponseEntity<Cliente> receberCliente(@RequestBody Cliente cliente) {
        Cliente salvo = service.salvar(cliente);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/cliente/exists/{cpf}/{cnpj}")
    public ResponseEntity<Boolean> existeCliente(
            @PathVariable(required = false) String cpf,
            @PathVariable(required = false) String cnpj) {
        boolean exists = service.existeCliente(cpf, cnpj);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = service.listaNaoSincronizados();
        service.marcarClienteComSync(clientes);
        return ResponseEntity.ok(clientes);
    }
}