package com.projeto.cadastroCliente.controller;

import com.projeto.cadastroCliente.exception.ClienteException;
import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sync")
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    @Autowired
    private ClienteService service;

    @PostMapping("/cliente")
    public ResponseEntity<?> receberCliente(@RequestBody Cliente cliente) {
        try {
            cliente.setId(null);
            Cliente salvo = service.salvar(cliente);
            return ResponseEntity.ok(salvo);
        } catch (ClienteException e) {
            logger.error("Erro de validação ao salvar cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado ao salvar cliente: {}", e.getMessage(), e);
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