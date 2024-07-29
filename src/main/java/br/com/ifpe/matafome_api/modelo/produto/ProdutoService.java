package br.com.ifpe.matafome_api.modelo.produto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Produto save(Produto produto) {

        produto.setHabilitado(Boolean.TRUE);
        produto.setVersao(1L);
        produto.setDataCriacao(LocalDate.now());
        Produto produtoSalvo = repository.save(produto);
 
        return produtoSalvo;
 
    }

    public List<Produto> listarTodos() {

        return repository.findAll();
    }

    public Produto obterPorID(Long id) {

        Optional<Produto> consulta = repository.findById(id);
  
       if (consulta.isPresent()) {
           return consulta.get();
       } else {
           throw new EntidadeNaoEncontradaException("produto", id);
       }

    }

    @Transactional
    public void update(Long id, Produto produtoAlterado) {

        Produto produto = repository.findById(id).get();
        produto.setNome(produtoAlterado.getNome());
        produto.setPreco(produtoAlterado.getPreco());
        produto.setCategoria(produtoAlterado.getCategoria());
        produto.setDescricao(produtoAlterado.getDescricao());
        produto.setImagem(produtoAlterado.getImagem());

        produto.setVersao(produto.getVersao() + 1);
        repository.save(produto);
    }

    @Transactional
    public void delete(Long id) {
    
        Produto produto = repository.findById(id).get();
        produto.setHabilitado(Boolean.FALSE);
        produto.setVersao(produto.getVersao() + 1);
    
        repository.save(produto);
    }


}
