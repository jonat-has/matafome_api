package br.com.ifpe.matafome_api.api.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas/{empresaId}/prateleiras/{prateleiraId}/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> listarTodosProdutos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterPorIDProdutos(@PathVariable Long id) {
        Produto produto = produtoService.obterPorID(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<Produto> adicionarProduto(@PathVariable("prateleiraId") Long prateleiraId, @RequestBody @Valid Produto produto) {
        Produto novoProduto = produtoService.adicionarProduto(prateleiraId, produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAlterado) {
        produtoService.update(id, produtoAlterado);
        Produto produtoAtualizado = produtoService.obterPorID(id);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{produtoId}/prateleira/{novaPrateleiraId}")
    public ResponseEntity<Produto> atualizarPrateleiraDoProduto(@PathVariable Long produtoId,
            @PathVariable Long novaPrateleiraId) {
        Produto produtoAtualizado = produtoService.atualizarPrateleira(produtoId, novaPrateleiraId);
        return ResponseEntity.ok(produtoAtualizado);
    }
}
