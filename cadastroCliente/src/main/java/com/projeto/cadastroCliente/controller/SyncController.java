package com.projeto.cadastroCliente.controller;

import com.projeto.cadastroCliente.model.Cliente;
import com.projeto.cadastroCliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    private ClienteRepository repository;

    @PostMapping("/cliente")
    public ResponseEntity<?> receberCliente(@RequestBody Cliente cliente) {
        try {
            cliente.setSync(true);
            repository.save(cliente);
            return ResponseEntity.ok("Cliente sincronizado com sucesso");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao sincronizar cliente");
        }
    }
}