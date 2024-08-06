package br.com.ifpe.matafome_api.modelo.categoria_produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Categoria_produtoRepository extends JpaRepository<Categoria_produto, Long> {

}
