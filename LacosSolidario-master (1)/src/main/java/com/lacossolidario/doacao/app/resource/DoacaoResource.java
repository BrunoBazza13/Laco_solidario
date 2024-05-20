package com.lacossolidario.doacao.app.resource;

import com.lacossolidario.doacao.app.dto.DadosListagemDoacoes;
import com.lacossolidario.doacao.domain.Doacao;
import com.lacossolidario.doacao.infra.model.DadosCadastroDoacao;
import com.lacossolidario.doacao.infra.repository.DoacaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/doacao")
public class DoacaoResource {

    @Autowired
    private DoacaoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity registrarDoacao(@RequestBody @Valid DadosCadastroDoacao dados, UriComponentsBuilder uriBuilder){
        var doacao = new Doacao(dados);
        repository.save(doacao);

        var uri = uriBuilder.path("/doacao/{id}").buildAndExpand(doacao.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosListagemDoacoes(doacao));

    }

    @GetMapping
    public ResponseEntity<List<DadosListagemDoacoes>> listarDoacoes(){
        var list = repository.findAll().stream()
                .map(DadosListagemDoacoes::new).toList();

        return ResponseEntity.ok(list);

    }

    @GetMapping("/{id}")
    public ResponseEntity detalharPorId(@PathVariable Long id){
        var doacao  = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosListagemDoacoes(doacao));

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        repository.deleteById(id);

        return ResponseEntity.noContent().build();

    }


}
