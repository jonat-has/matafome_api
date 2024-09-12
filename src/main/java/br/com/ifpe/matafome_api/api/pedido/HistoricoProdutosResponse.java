package br.com.ifpe.matafome_api.api.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.PrateleirasMaisLucrativas;
import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.PrateleirasMaisVendidas;
import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.ProdutosMaisLucrativos;
import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.ProdutosMaisVendidos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoProdutosResponse {

    private  List<ProdutosMaisVendidos> produtosMaisVendidos;
    private  List<ProdutosMaisLucrativos> produtosMaisLucrativos;
    private  List<PrateleirasMaisVendidas> prateleirasMaisVendidas;
    private  List<PrateleirasMaisLucrativas> prateleirasMaisLucrativas;
}
