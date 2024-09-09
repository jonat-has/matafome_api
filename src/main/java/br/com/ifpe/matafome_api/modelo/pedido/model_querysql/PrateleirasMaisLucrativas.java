package br.com.ifpe.matafome_api.modelo.pedido.model_querysql;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrateleirasMaisLucrativas {

    private String nomeCategoria;
    private Double valorTotal;

}
