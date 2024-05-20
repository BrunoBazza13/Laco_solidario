package com.lacossolidario.doacao.domain;

import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_doadores")
@PrimaryKeyJoinColumn(name = "usuario_id")
@NoArgsConstructor
@AllArgsConstructor
public class Doador extends Usuario {

    private String cpf;
    
    
    
    public Doador() {
		super();
	}

	public Doador(DadosCadastroUsuario dados, String cpf) {
        super(dados);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public String getLogin() {
        return super.getLogin();
    }

}
