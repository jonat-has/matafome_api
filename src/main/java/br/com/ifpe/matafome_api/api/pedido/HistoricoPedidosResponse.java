package br.com.ifpe.matafome_api.api.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.PedidosPorDia;
import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.UltimosClientes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPedidosResponse {

    private Double totalVendas;
    private Long quantidadeClientes;
    private Long quantidadePedidos;
    private Long pedidosHoje;
    private List<PedidosPorDia> pedidosUltimos7Dias;
    private List<UltimosClientes> ultimasVendas;


}
