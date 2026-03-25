package com.projeto.cadastroCliente.dto;

import com.projeto.cadastroCliente.model.enums.TipoDePessoa;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteDto(@NotBlank(message = "O nome do cliente nao pode estar em branco")
                         String nomeDoCliente,

                         @NotNull(message = "O tipo de pessoa nao pode estar em branco")
                         TipoDePessoa tipoDePessoa,

                         @NotBlank(message = "O e-mail não pode estar em branco")
                         String cpfCnpj,

                         @NotBlank(message = "O e-mail não pode estar em branco")
                         String telefone,

                         @Email(message = "E-mail inválido")
                         String email) {

    public ClienteDto {
        if (tipoDePessoa == TipoDePessoa.FISICA) {
            if (!validarCPF(cpfCnpj)) {
                throw new IllegalArgumentException("CPF inválido: " + cpfCnpj);
            }
        } else if (tipoDePessoa == TipoDePessoa.JURIDICA) {
            if (!validarCNPJ(cpfCnpj)) {
                throw new IllegalArgumentException("CNPJ inválido: " + cpfCnpj);
            }
        }

        cpfCnpj = cpfCnpj.replaceAll("\\D", "");
    }

    private static boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;

            return (cpf.charAt(9) - '0' == primeiroDigito) &&
                    (cpf.charAt(10) - '0' == segundoDigito);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean validarCNPJ(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * peso1[i];
            }
            int primeiroDigito = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * peso2[i];
            }
            int segundoDigito = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            return (cnpj.charAt(12) - '0' == primeiroDigito) &&
                    (cnpj.charAt(13) - '0' == segundoDigito);
        } catch (Exception e) {
            return false;
        }
    }
}
