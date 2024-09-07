package br.com.ifpe.matafome_api.modelo.pedido;

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

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.PedidosPorDia(p.dataCriacao, SUM(p.valorTotal)) " +
            "FROM Pedido p WHERE p.empresa.id = :empresaId AND p.dataCriacao BETWEEN :startDate AND :endDate AND p.status = 'ENTREGUE' " +
            "GROUP BY p.dataCriacao")
    List<PedidosPorDia> findPedidosUltimos7Dias(@Param("empresaId") Long empresaId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate);

    @Query("SELECT new br.com.ifpe.matafome_api.modelo.pedido.UltimosClientes(p.cliente.nome, p.cliente.foneCelular, p.valorTotal) " +
            "FROM Pedido p WHERE p.empresa.id = :empresaId AND p.status = 'ENTREGUE' " +
            "ORDER BY p.dataCriacao DESC")
    List<UltimosClientes> findUltimasVendas(@Param("empresaId") Long empresaId);

}

