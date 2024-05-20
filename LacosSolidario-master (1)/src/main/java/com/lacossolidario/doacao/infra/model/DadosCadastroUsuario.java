package com.lacossolidario.doacao.infra.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public record DadosCadastroUsuario(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String login,
        @NotBlank
        String senha,
        @NotBlank
        String telefone,
        @NotBlank
        String tipoDeUsuario,
        @CPF
        String cpf,
        @CNPJ
        String cnpj


) {

        public String getCpf() {
                return cpf;
        }

        public String getCnpj() {
                return cnpj;
        }

		public String getsenha() {
			return senha;
		}

		public String nome() {
			return nome;
		}

		public String login() {
			return login;
		}

		public String senha() {
			return senha;
		}

		public String telefone() {
			return telefone;
		}

		public String tipoDeUsuario() {
			return tipoDeUsuario;
		}

		public String cpf() {
			return cpf;
		}

		public String cnpj() {
			return cnpj;
		}

		

		

    
}
