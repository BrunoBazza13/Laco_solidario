package com.lacossolidario.doacao.app.dto;

import com.lacossolidario.doacao.infra.model.DadosEndereco;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
public record DadosAtualizacaoUsuario(
        @NotNull
        Long id,
        String nome,
        @Email
        String login,
        String senha,
        String telefone
//       @Lob
//        byte[] imagem

) {
}
