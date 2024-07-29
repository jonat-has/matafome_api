package br.com.ifpe.matafome_api.api.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.modelo.produto.Produto;
import br.com.ifpe.matafome_api.modelo.produto.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(
       summary = "Serviço responsável por salvar um produto no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir um produto no sistema."
   )
    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody @Valid ProdutoRequest request) {

        Produto produto = produtoService.save(request.build());
        return new ResponseEntity<Produto>(produto, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }   

    @GetMapping("/{id}")
    public Produto obterPorID(@PathVariable Long id) {
        return produtoService.obterPorID(id);
    }   

     @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable("id") Long id, @RequestBody ProdutoRequest request) {   
       produtoService.update(id, request.build());
       return ResponseEntity.ok().build();
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       produtoService.delete(id);
       return ResponseEntity.ok().build();
   }



}
