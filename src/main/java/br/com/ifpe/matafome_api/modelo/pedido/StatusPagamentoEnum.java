package br.com.ifpe.matafome_api.modelo.pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusPagamentoEnum {

    PENDENTE("Pendente"),
    EM_ANALISE("Em Análise"),
    APROVADO("Aprovado"),
    RECUSADO("Recusado"),
    CANCELADO("Cancelado"),
    REEMBOLSADO("Reembolsado"),
    FALHA("Falha");


    private final String descricao;

}
