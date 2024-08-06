package br.com.ifpe.matafome_api.api.categoria_produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifpe.matafome_api.modelo.categoria_produto.Categoria_produto;
import br.com.ifpe.matafome_api.modelo.categoria_produto.Categoria_produtoService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categoriaProduto")
@CrossOrigin
public class Categoria_produtoController {

    @Autowired
    private Categoria_produtoService categoria_produtoService;


    @PostMapping
    public ResponseEntity<Categoria_produto> save(@RequestBody @Valid Categoria_produtoRequest request) {
 
        Categoria_produto categoria_produtoNovo = request.build();
        Categoria_produto categoria_produto = categoria_produtoService.save(categoria_produtoNovo);
        return new ResponseEntity<Categoria_produto>(categoria_produto, HttpStatus.CREATED);
    }
 

    @GetMapping
    public List<Categoria_produto> listarTodos() {
        return categoria_produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Categoria_produto obterPorID(@PathVariable Long id) {
        return categoria_produtoService.obterPorID(id);
    }

    @PutMapping("/{id}")
   public ResponseEntity<Categoria_produto> update(@PathVariable("id") Long id, @RequestBody Categoria_produtoRequest request) {

       Categoria_produto categoria_produto = request.build();
       categoria_produtoService.update(id, categoria_produto);
      
       return ResponseEntity.ok().build();
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoria_produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
