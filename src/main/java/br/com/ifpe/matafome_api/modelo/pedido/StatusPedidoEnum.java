package br.com.ifpe.matafome_api.modelo.pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPedidoEnum {
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    EM_TRANSITO("Em Trânsito"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");

    private final String descricao;
}
