package com.lacossolidario.doacao.app.dto;

import com.lacossolidario.doacao.domain.Doador;
import com.lacossolidario.doacao.domain.Usuario;

public record DadosListagemUsuario(
         Long id,
         String nome,
         String login,
         String senha,
         String telefone,
        String cpf,
        byte [] imagem
        
) {

    public DadosListagemUsuario(Doador usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getLogin(), usuario.getSenha(),
                usuario.getTelefone(), usuario.getCpf(), usuario.getImagem());


    }

	

	
}
