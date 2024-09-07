package br.com.ifpe.matafome_api.modelo.pedido;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UltimosClientes {

    private String nomeCliente;
    private String numeroTelefone;
    private Double valorDoPedido;

}
