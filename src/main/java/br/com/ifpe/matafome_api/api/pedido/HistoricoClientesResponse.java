package br.com.ifpe.matafome_api.api.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.BairrosFrequentes;
import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.ClientesFrequentes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoClientesResponse {

    private Long novosClientes;
    private Long clientesAtivos;
    private Double taxaRentecao;
    private Long cepsAtendidos;
    private List<ClientesFrequentes> clientesMaisFrequentes;
    private List<BairrosFrequentes> bairrosMaisFrequentes;


}
