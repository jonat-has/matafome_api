package br.com.ifpe.matafome_api.modelo.pedido.model_querysql;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UltimosClientes {

    private String nomeCliente;
    private String numeroTelefone;
    private Double valorDoPedido;
    private LocalDateTime dataHoraPedido;

}
