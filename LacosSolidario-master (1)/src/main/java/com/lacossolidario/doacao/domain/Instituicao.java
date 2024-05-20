package com.lacossolidario.doacao.domain;

import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_instituicoes")
@PrimaryKeyJoinColumn(name = "usuario_id")
@NoArgsConstructor
@AllArgsConstructor
public class Instituicao extends Usuario {

	private String cnpj;

	public Instituicao(DadosCadastroUsuario dados, String cnpj) {
		super(dados);
		this.cnpj = cnpj;
	
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	
	
	
}
