package br.com.ifpe.matafome_api.modelo.produto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.modelo.prateleira.PrateleiraRepository;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PrateleiraRepository prateleiraRepository;

    @Transactional
    public Produto adicionarProduto(Long prateleiraId, Produto produto) {

        produto.setHabilitado(Boolean.TRUE);
        produto.setVersao(1L);
        produto.setDataCriacao(LocalDate.now());

        // Verifica se a prateleira existe
        Prateleira prateleira = prateleiraRepository.findById(prateleiraId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Prateleira não encontrada", prateleiraId));

        // Associa o produto à prateleira
        produto.setPrateleira(prateleira);

        // Salva o produto
        return produtoRepository.save(produto);
    }


    @Transactional
    public Produto atualizarPrateleira(Long produtoId, Long novaPrateleiraId) {
        // Verifica se o produto existe
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado", novaPrateleiraId));

        // Verifica se a nova prateleira existe
        Prateleira novaPrateleira = prateleiraRepository.findById(novaPrateleiraId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Prateleira não encontrada", novaPrateleiraId));

        // Atualiza a prateleira do produto
        produto.setPrateleira(novaPrateleira);

        // Salva as alterações
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto obterPorID(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado", id));
    }

    @Transactional
    public Produto update(Long id, Produto produtoAlterado) {
        Produto produto = obterPorID(id);
        produto.setNome(produtoAlterado.getNome());
        produto.setPreco(produtoAlterado.getPreco());
        produto.setDescricao(produtoAlterado.getDescricao());
        produto.setUrlImagem(produtoAlterado.getUrlImagem());
        return produtoRepository.save(produto);
    }

    @Transactional
    public void delete(Long id) {
        Produto produto = obterPorID(id);
        produtoRepository.delete(produto);
    }
}
