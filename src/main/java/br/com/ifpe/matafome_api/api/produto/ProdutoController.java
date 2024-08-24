package br.com.ifpe.matafome_api.api.produto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public Page<Produto> findAllProdutos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return produtoService.findAllProdutos(page, size);
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

        @GetMapping("/buscar")
    public Page<Produto> buscarPorNome(
            @RequestParam("nome") String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return produtoService.buscarPorNome(nome,  page, size);
    }

    // Endpoint para buscar por nome e prateleira com paginação
    @GetMapping("/buscarPorNomeEPrateleira")
    public Page<Produto> buscarPorNomeEPrateleira(
            @RequestParam("nome") String nome,
            @RequestParam("nomePrateleira") String nomePrateleira,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
                return produtoService.buscarPorNomeEPrateleira(nome, nomePrateleira, page, size);
            }
}
