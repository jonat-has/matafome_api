package br.com.ifpe.matafome_api.api.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import br.com.ifpe.matafome_api.modelo.produto.Produto;
import br.com.ifpe.matafome_api.modelo.produto.ProdutoService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

  
    @PostMapping
    public ResponseEntity<Produto> save(@RequestBody @Valid ProdutoRequest request) {
 
        Produto produtoNovo = request.build();
        Produto produto = produtoService.save(produtoNovo);
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

       Produto produto = request.build();
       produtoService.update(id, produto);
      
       return ResponseEntity.ok().build();
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
