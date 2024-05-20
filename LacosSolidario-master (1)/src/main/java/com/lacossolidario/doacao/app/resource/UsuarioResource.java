package com.lacossolidario.doacao.app.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.lacossolidario.doacao.app.dto.DadosAtualizacaoUsuario;
import com.lacossolidario.doacao.app.dto.DadosListagemUsuario;
import com.lacossolidario.doacao.domain.Doador;
import com.lacossolidario.doacao.domain.Instituicao;
//import com.lacossolidario.doacao.domain.Instituicao;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.model.DadosLogin;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;
import com.lacossolidario.doacao.infra.service.DoadorService;
import com.lacossolidario.doacao.infra.service.TokenService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioResource {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private DoadorService usuarioService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	@RequestMapping("/cadastro/{tipoDeUsuario}")
	@Transactional
	public ResponseEntity cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados,
			@PathVariable String tipoDeUsuario, UriComponentsBuilder uriBuilder) {
		Usuario usuario = null;

		if (!tipoDeUsuario.equalsIgnoreCase("instituicao") && !tipoDeUsuario.equalsIgnoreCase("doador")) {
			return ResponseEntity.badRequest().body("Tipo de usuário inválido");
		}

		if (tipoDeUsuario.equalsIgnoreCase("instituicao")) {
			if (dados.getCnpj() == null || dados.getCnpj().isEmpty()) {
				return ResponseEntity.badRequest().body("CNPJ não fornecido");
			}
			if (dados.getCpf() != null && !dados.getCpf().isEmpty()) {
				return ResponseEntity.badRequest().body("Não é possível cadastrar um CPF para uma instituição");
			}
			if (repository.existsInstituicaoByCnpj(dados.getCnpj())) {
				return ResponseEntity.badRequest().body("Já existe uma instituição cadastrada com este CNPJ");
			}
			usuario = new Instituicao(dados, dados.getCnpj());
		} else if (tipoDeUsuario.equalsIgnoreCase("doador")) {
			if (dados.getCpf() == null || dados.getCpf().isEmpty()) {
				return ResponseEntity.badRequest().body("CPF não fornecido");
			}
			if (dados.getCnpj() != null && !dados.getCnpj().isEmpty()) {
				return ResponseEntity.badRequest().body("Não é possível cadastrar um CNPJ para um doador");
			}
			if (repository.existsDoadorByCpf(dados.getCpf())) {
				return ResponseEntity.badRequest().body("Já existe um doador cadastrado com este CPF");
			}
			usuario = new Doador(dados, dados.getCpf());
		}

		if (!usuario.getTipoDeUsuario().equalsIgnoreCase(tipoDeUsuario)) {
			return ResponseEntity.badRequest().body("Tipo de usuário inconsistente com os dados fornecidos");
		}

		usuarioService.criaUsuario(usuario);

//
        var uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}

	@PostMapping
	@RequestMapping("/login")
	@Transactional
	public ResponseEntity login(@RequestBody @Valid DadosLogin data) throws Exception {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				data.login(), data.senha());

		Authentication auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

		if (token == null) {
			throw new Exception("Senha ou email invalidos");
			// return null;
		} else {
			Usuario usuario = (Usuario) auth.getPrincipal();
			Long userId = usuario.getId();
			
//			 Map<String, Object> response = new HashMap<>();
//		        response.put("token", token);
//		        response.put("usuarioId", userId);
			
			
			
			return ResponseEntity.status(HttpStatus.OK).body(userId);
		}
	}

//	@GetMapping("/listar/{tipoDeUsuario}")
//	public ResponseEntity<List<DadosListagemUsuario>> listarUsuario(@PathVariable String tipoDeUsuario) {
//		var list = repository.findAllByAtivoTrueAndTipoDeUsuario(tipoDeUsuario).stream().map(DadosListagemUsuario::new)
//				.toList();
//
//		return ResponseEntity.ok(list);
//	}


	@GetMapping("/{id}")
	public ResponseEntity detalharPorId(@PathVariable Long id) {
	    Optional<Usuario> optionalUsuario = repository.findById(id);
	    
	    if (optionalUsuario.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	    }

	    Usuario usuario = optionalUsuario.get();
	    
	    if (usuario instanceof HibernateProxy) {
	        usuario = (Usuario) ((HibernateProxy) usuario).getHibernateLazyInitializer().getImplementation();
	    }
	    
	    if (usuario instanceof Doador) {
	        Doador doador = (Doador) usuario;
	        return ResponseEntity.ok(new DadosListagemUsuario(doador));
	    }

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoUsuario dados, @RequestPart("imagem") MultipartFile imagem) {
	    try {
	    	
	    	 Optional<Usuario> optionalUsuario = repository.findById(id);
	    	 	    
	    	 
	    	 if (optionalUsuario.isEmpty()) {
	    		 System.out.println("chegou aqui");
	 	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	 	    }
	    	 
	    	 Usuario usuario = optionalUsuario.get();
	    	 usuario.setImagem(imagem.getBytes());
	    	 
	    	 if (usuario instanceof HibernateProxy) {
	 	        usuario = (Usuario) ((HibernateProxy) usuario).getHibernateLazyInitializer().getImplementation();
	 	    }
	    	 
	    	 if (usuario instanceof Doador) {
	    		 System.err.println("chegou aqui");
	    		 Doador doador = (Doador) usuario;
	             doador.atualizarDados(dados, imagem);
	           
	             repository.save(doador);
	    	 }
	    	
	    }catch (Exception e) {
	    	  e.printStackTrace();
		       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a imagem: " + e.getMessage());
	    	
		}
		return null;
	       
	        
	     
	}
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity desativarUsuario(@PathVariable Long id) {
		var usuario = repository.getReferenceById(id);
		usuario.desativar();

		return ResponseEntity.noContent().build();

	}
}
