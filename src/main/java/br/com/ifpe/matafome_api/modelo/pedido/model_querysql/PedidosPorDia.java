package br.com.ifpe.matafome_api.modelo.pedido.model_querysql;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidosPorDia {

    private LocalDate dia;
    private Double valorTotal;

}
