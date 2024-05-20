package com.lacossolidario.doacao.domain;

import com.lacossolidario.doacao.infra.model.DadosCadastroDoacao;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tb_doacoes")
@Entity(name = "Doacao")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoria;
    private String descricao;
    private String data;

    @Embedded
    private Endereco endereco;

    public Doacao( DadosCadastroDoacao dados) {
        this.categoria = dados.categoria();
        this.descricao = dados.descricao();
        this.data = dados.data();
        this.endereco = new Endereco(dados.endereco());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
