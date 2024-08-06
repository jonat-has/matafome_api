package br.com.ifpe.matafome_api.modelo.categoria_produto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class Categoria_produtoService {

    @Autowired
    private Categoria_produtoRepository repository;

    @Transactional
    public Categoria_produto save(Categoria_produto categoria_produto) {

        categoria_produto.setHabilitado(Boolean.TRUE);
        categoria_produto.setVersao(1L);
        categoria_produto.setDataCriacao(LocalDate.now());
        Categoria_produto categoria_produtoSalvo = repository.save(categoria_produto);

        return categoria_produtoSalvo;
    }

    public List<Categoria_produto> listarTodos() {
        return repository.findAll();
    }

    public Categoria_produto obterPorID(Long id) {
        Optional<Categoria_produto> consulta = repository.findById(id);

        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("Categoria_produto", id);
        }
    }

   @Transactional
    public void update(Long id, Categoria_produto categoria_produtoAlterado) {

        Categoria_produto categoria_produto = repository.findById(id).get();
        categoria_produto.setNome_categoria(categoria_produto.getNome_categoria());
        categoria_produto.setVersao(categoria_produto.getVersao() + 1);
        repository.save(categoria_produto);
    }


    @Transactional
    public void delete(Long id) {
        Categoria_produto categoria_produto = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria_produto", id));
        categoria_produto.setHabilitado(Boolean.FALSE);
        categoria_produto.setVersao(categoria_produto.getVersao() + 1);

        repository.save(categoria_produto);
    }
}
