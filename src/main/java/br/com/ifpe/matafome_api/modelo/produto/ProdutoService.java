package br.com.ifpe.matafome_api.modelo.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;

import br.com.ifpe.matafome_api.modelo.prateleira.PrateleiraRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PrateleiraRepository prateleiraRepository;

    public Produto criarProduto(Long prateleiraId, Produto produto) {
        Prateleira prateleira = prateleiraRepository.findById(prateleiraId).orElseThrow(() -> new RuntimeException("Prateleira não encontrada"));
        produto.setPrateleira(prateleira);
        return produtoRepository.save(produto);
    }

    public Produto atualizarCategoria(Long produtoId, Long novaPrateleiraId) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Prateleira novaPrateleira = prateleiraRepository.findById(novaPrateleiraId).orElseThrow(() -> new RuntimeException("Prateleira não encontrada"));
        produto.setPrateleira(novaPrateleira);
        return produtoRepository.save(produto);
    }
}
