package br.com.ifpe.matafome_api.modelo.pedido.model_querysql;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientesFrequentes {

    private String nome;
    private String bairro;
    private Long totalPedidos;
    private Double totalPago;
}
