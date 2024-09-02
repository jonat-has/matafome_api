package br.com.ifpe.matafome_api.modelo.produto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    @Autowired
    private AdicionaisRepository adicionaisRepository;


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

    public Page<Produto> findAllProdutos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.findAll(pageable);
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

    public Page<Produto> buscarPorNome(String nome, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.findByNome(nome, pageable);
    }

    public Page<Produto> buscarPorNomeEPrateleira(String nome, String nomePrateleira, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.findByNomeAndPrateleira(nome, nomePrateleira, pageable);
    }

    /* FUNÇÔES DE ADICIONAIS*/

    public List<Adicionais> getAdicionais() {
        return adicionaisRepository.findAll();
    }

    @Transactional
    public Adicionais adicionarAdicionais(Long produtoId, Adicionais adicionais) {


        adicionais.setHabilitado(Boolean.TRUE);
        adicionais.setVersao(1L);
        adicionais.setDataCriacao(LocalDate.now());


        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrada", produtoId));

        System.out.println(produto);
        adicionais.setProduto(produto);


        return adicionaisRepository.save(adicionais);
    }

    @Transactional
    public HashMap<String, Object> obterTodosAdicionaisProduto(Long idProduto){

        Produto produto = this.obterPorID(idProduto);

        HashMap<String, Object> produto_id = new HashMap<>();

        List<Adicionais> listaAddProduto = produto.getAdicionais();

        if (listaAddProduto == null) {

            listaAddProduto = new ArrayList<>();

        }

        produto_id.put("idProduto", idProduto);
        produto_id.put("adicionais", listaAddProduto);

        return produto_id;

    }
}
