package br.com.ifpe.matafome_api.modelo.pedido.model_querysql;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutosMaisLucrativos {

    private String nomeProduto;
    private Double valorTotal;

}
