package com.projeto.cadastroCliente.controller;

import com.projeto.cadastroCliente.exception.ClienteException;
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
    public ResponseEntity<?> receberCliente(@RequestBody Cliente cliente) {
        try {
            cliente.setId(null);

            String cpf = cliente.getCpf();
            String cnpj = cliente.getCnpj();

            if (!cliente.isDeletado() && service.existeClienteAtivo(cpf, cnpj)) {
                Cliente salvo = service.salvar(cliente);
                return ResponseEntity.ok(salvo);
            }

            Cliente salvo = service.salvar(cliente);
            return ResponseEntity.ok(salvo);
        } catch (ClienteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = service.listaNaoSincronizados();
        service.marcarClienteComSync(clientes);
        return ResponseEntity.ok(clientes);
    }
}