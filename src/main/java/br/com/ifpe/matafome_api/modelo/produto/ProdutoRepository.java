package br.com.ifpe.matafome_api.modelo.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %:nome%")
        Page<Produto> findByNome(@Param("nome") String nome, Pageable pageable);

    // Filtro por nome do produto e prateleira com paginação
    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %:nome% AND p.prateleira.nomePrateleira = :nomePrateleira")
    Page<Produto> findByNomeAndPrateleira(@Param("nome") String nome, @Param("nomePrateleira") String nomePrateleira, Pageable pageable);

    Page<Produto> findAll(Pageable pageable);

}
