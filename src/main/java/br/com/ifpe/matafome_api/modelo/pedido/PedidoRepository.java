package br.com.ifpe.matafome_api.modelo.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

import java.util.List;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.empresa.id = :empresaId AND p.dataCriacao BETWEEN :startDate AND :endDate AND p.status = 'ENTREGUE'")
    Double findTotalVendas(@Param("empresaId") Long empresaId,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT p.cliente.id) FROM Pedido p WHERE p.empresa.id = :empresaId AND p.dataCriacao BETWEEN :startDate AND :endDate AND p.status = 'ENTREGUE'")
    Long countClientes(@Param("empresaId") Long empresaId,
                       @Param("startDate") LocalDate startDate,
                       @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.empresa.id = :empresaId AND p.dataCriacao BETWEEN :startDate AND :endDate AND p.status = 'ENTREGUE'")
    Long countPedidos(@Param("empresaId") Long empresaId,
                      @Param("startDate") LocalDate startDate,
                      @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.empresa.id = :empresaId AND p.dataCriacao = :today AND p.status = 'ENTREGUE'")
    Long countPedidosHoje(@Param("empresaId") Long empresaId,
                          @Param("today") LocalDate today);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.PedidosPorDia(p.dataCriacao, SUM(p.valorTotal)) " +
            "FROM Pedido p WHERE p.empresa.id = :empresaId AND p.dataCriacao BETWEEN :startDate AND :endDate AND p.status = 'ENTREGUE' " +
            "GROUP BY p.dataCriacao")
    List<PedidosPorDia> findPedidosUltimos7Dias(@Param("empresaId") Long empresaId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.UltimosClientes(p.cliente.nome, p.cliente.foneCelular, p.valorTotal, p.dataHoraPedido) " +
            "FROM Pedido p WHERE p.empresa.id = :empresaId AND p.status = 'ENTREGUE' " +
            "ORDER BY p.dataCriacao DESC")
    List<UltimosClientes> findUltimasVendas(@Param("empresaId") Long empresaId, Pageable pageable);


    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.ProdutosMaisVendidos(p.nome, SUM(i.quantidade)) " +
            "FROM Pedido ped JOIN ped.itensPedido i JOIN i.produto p " +
            "WHERE ped.empresa.id = :empresaId AND ped.dataCriacao BETWEEN :startDate AND :endDate AND ped.status = 'ENTREGUE' " +
            "GROUP BY p.id " +
            "ORDER BY SUM(i.quantidade) DESC")
    List<ProdutosMaisVendidos> findProdutoMaisVendido(@Param("empresaId") Long empresaId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.ProdutosMaisLucrativos(p.nome, SUM(p.preco * i.quantidade)) " +
            "FROM Pedido ped JOIN ped.itensPedido i JOIN i.produto p " +
            "WHERE ped.empresa.id = :empresaId AND ped.dataCriacao BETWEEN :startDate AND :endDate AND ped.status = 'ENTREGUE'" +
            "GROUP BY p.id " +
            "ORDER BY SUM(i.quantidade * p.preco) DESC")
    List<ProdutosMaisLucrativos> findProdutoMaisLucrativo(@Param("empresaId") Long empresaId,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.PrateleirasMaisVendidas(pr.nomePrateleira, SUM(i.quantidade))" +
            "FROM Pedido ped JOIN ped.itensPedido i JOIN i.produto p JOIN p.prateleira pr " +
            "WHERE ped.empresa.id = :empresaId AND ped.dataCriacao BETWEEN :startDate AND :endDate AND ped.status = 'ENTREGUE'" +
            "GROUP BY pr.id " +
            "ORDER BY SUM(i.quantidade) DESC")
    List<PrateleirasMaisVendidas> findPrateleiraMaisVendida(@Param("empresaId") Long empresaId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.PrateleirasMaisLucrativas(pr.nomePrateleira, SUM(i.quantidade * p.preco)) " +
            "FROM Pedido ped JOIN ped.itensPedido i JOIN i.produto p JOIN p.prateleira pr " +
            "WHERE ped.empresa.id = :empresaId AND ped.dataCriacao BETWEEN :startDate AND :endDate AND ped.status = 'ENTREGUE'" +
            "GROUP BY pr.id " +
            "ORDER BY SUM(i.quantidade * p.preco) DESC")
    List<PrateleirasMaisLucrativas> findPrateleirasMaisLucrativas(@Param("empresaId") Long empresaId,
                                                                  @Param("startDate") LocalDate startDate,
                                                                  @Param("endDate") LocalDate endDate, Pageable pageable);


    // Novos clientes que fizeram o primeiro pedido no período
    @Query("SELECT COUNT(DISTINCT p.cliente) " +
            "FROM Pedido p " +
            "WHERE p.empresa.id = :idEmpresa " +
            "AND p.dataCriacao BETWEEN :startDate AND :endDate " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM Pedido p2 " +
            "   WHERE p2.cliente = p.cliente " +
            "   AND p2.empresa.id = :idEmpresa " +
            "   AND p2.dataCriacao < :startDate)")
    Long findNovosClientes(@Param("idEmpresa") Long idEmpresa,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT p.cliente) " +
            "FROM Pedido p " +
            "WHERE p.empresa.id = :idEmpresa " +
            "AND p.cliente IN (" +
            "  SELECT p1.cliente " +
            "  FROM Pedido p1 " +
            "  WHERE p1.empresa.id = :idEmpresa " +
            "  AND p1.dataCriacao BETWEEN :startDate AND :endDate " +
            "  GROUP BY p1.cliente " +
            "  HAVING COUNT(p1.id) > 1" +
            ")")
    Long countClientesAtivos(@Param("idEmpresa") Long idEmpresa,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate);


    // Contagem de CEPs distintos atendidos no período
    @Query("SELECT COUNT(DISTINCT p.enderecoEntrega.cep) FROM Pedido p WHERE p.empresa.id = :idEmpresa AND p.dataCriacao BETWEEN :startDate AND :endDate")
    Long countDistinctCeps(@Param("idEmpresa") Long idEmpresa, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.ClientesFrequentes(p.cliente.id, p.cliente.nome, p.enderecoEntrega.bairro, COUNT(p), SUM(p.valorTotal)) " +
            "FROM Pedido p WHERE p.empresa.id = :idEmpresa AND p.dataCriacao BETWEEN :startDate AND :endDate " +
            "GROUP BY p.cliente.id, p.cliente.nome, p.enderecoEntrega.bairro ORDER BY COUNT(p) DESC")
    List<ClientesFrequentes> findClientesMaisFrequentes(@Param("idEmpresa") Long idEmpresa, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);



    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.model_querysql.BairrosFrequentes(p.enderecoEntrega.bairro, COUNT(p)) " +
            "FROM Pedido p WHERE p.empresa.id = :idEmpresa AND p.dataCriacao BETWEEN :startDate AND :endDate " +
            "GROUP BY p.enderecoEntrega.bairro ORDER BY COUNT(p) DESC")
    List<BairrosFrequentes> findBairrosMaisFrequentes(@Param("idEmpresa") Long idEmpresa, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
}