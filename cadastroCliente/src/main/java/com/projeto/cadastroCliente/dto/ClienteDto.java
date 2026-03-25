package com.projeto.cadastroCliente.dto;

import com.projeto.cadastroCliente.model.enums.CpfCnpj;
import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteDto(@NotBlank String nomeDoCliente,
                         @NotNull TipoDePessoa tipoDePessoa,
                         @NotNull CpfCnpj cpfCnpj,
                         @NotBlank String telefone,
                         String email) {
}
