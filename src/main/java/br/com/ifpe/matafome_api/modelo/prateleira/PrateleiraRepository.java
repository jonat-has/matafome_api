package br.com.ifpe.matafome_api.modelo.prateleira;


import br.com.ifpe.matafome_api.modelo.prateleira.model_querysql.PrateleirasPromocionais;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrateleiraRepository extends JpaRepository<Prateleira, Long> {

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.prateleira.model_querysql.PrateleirasPromocionais(p, e.id, e.nomeFantasia) " +
            "FROM Prateleira p JOIN p.empresa e " +
            "WHERE (p.nomePrateleira LIKE %:keyword1% OR p.nomePrateleira LIKE %:keyword2% OR p.nomePrateleira LIKE %:keyword3%) " +
            "AND p.habilitado = true")
    List<PrateleirasPromocionais> findPrateleirasComNomesSemelhantes(@Param("keyword1") String keyword1,
                                                                     @Param("keyword2") String keyword2,
                                                                     @Param("keyword3") String keyword3,
                                                                     Pageable pageable);



}
