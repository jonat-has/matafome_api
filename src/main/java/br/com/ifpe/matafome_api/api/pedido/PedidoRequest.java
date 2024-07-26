package br.com.ifpe.matafome_api.api.pedido;

import java.time.LocalTime;

import br.com.ifpe.matafome_api.modelo.pedido.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    
    private LocalTime hora_pedido;

    private String forma_pagamento;

    private String status_pagamento;

    private String horario;

    private Double valor_total;

    private LocalTime taxa_entrega;

        public Pedido build() {
            return Pedido.builder()
                .hora_pedido(hora_pedido)
                .forma_pagamento(forma_pagamento)
                .status_pagamento(status_pagamento)
                .horario(horario)
                .valor_total(valor_total)
                .taxa_entrega(taxa_entrega)
                .build();
        }
}
