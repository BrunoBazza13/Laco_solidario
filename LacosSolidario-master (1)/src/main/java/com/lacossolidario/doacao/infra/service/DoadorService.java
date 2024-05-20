package com.lacossolidario.doacao.infra.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lacossolidario.doacao.domain.Doador;
import com.lacossolidario.doacao.domain.Instituicao;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;

@Service
public class DoadorService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	
	public Usuario criaUsuario(Usuario novoUsuario) {
        if (novoUsuario instanceof Doador) {
            Doador doador = (Doador) novoUsuario;
            if (usuarioRepository.existsDoadorByCpf(doador.getCpf())) {
                throw new IllegalArgumentException("Já existe um doador cadastrado com este CPF");
            }
        } else if (novoUsuario instanceof Instituicao) {
            Instituicao instituicao = (Instituicao) novoUsuario;
            if (usuarioRepository.existsInstituicaoByCnpj(instituicao.getCnpj())) {
                throw new IllegalArgumentException("Já existe uma instituição cadastrada com este CNPJ");
            }
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(novoUsuario.getSenha());
        novoUsuario.setSenha(encryptedPassword);

        return usuarioRepository.save(novoUsuario);
    }


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return usuarioRepository.findByLogin(username);
	}


	
}
