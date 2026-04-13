package com.projeto.cadastroCliente.dto;

import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteDto(
        @NotBlank(message = "O nome do cliente não pode estar em branco")
        String nomeDoCliente,

        @NotNull(message = "O tipo de pessoa não pode estar em branco")
        TipoDePessoa tipoDePessoa,

        @NotBlank(message = "O CPF ou CNPJ não pode estar em branco")
        String cpfCnpj,

        @NotBlank(message = "O telefone não pode estar em branco")
        String telefone,

        String email) {
}