package br.com.ifpe.matafome_api.api.pedido;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    private Long clienteId;

    private Long empresaId;

    private Long enderecoEntregaId;

    private Long formaPagamentoId;

    private List<ItemPedidoRequest> itens;

    private String statusPagamento;

    private Float taxaEntrega;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoRequest {

        private Long produtoId;

        private Integer quantidade;
        
    }
}
