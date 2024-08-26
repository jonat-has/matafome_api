package br.com.ifpe.matafome_api.api.pedido;

import br.com.ifpe.matafome_api.modelo.pedido.StatusPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {

    private Long id;

    private ClienteResponse cliente;

    private EmpresaResponse empresa;

    private EnderecoClienteResponse enderecoEntrega;
    private FormaPagamentoResponse formaPagamento;
    private List<ItensPedidoResponse> itensPedido;
    private StatusPedidoEnum status;
    private LocalDateTime dataHoraPedido;
    private String statusPagamento;
    private Float taxaEntrega;
    private Double valorTotal;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteResponse {
        private Long id;
        private String nome;
        private String foneCelular;
        private String cpf;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmpresaResponse {
        private Long id;
        private String nomeFantasia;
        private LocalTime horarioAbertura;
        private LocalTime horarioFechamento;
        private LocalTime tempoEntrega;
        private String imgCapa;
        private String imgPerfil;
        private String categoria;
        private String telefone;
        private Double taxaFrete;
        private EnderecoResponse endereco;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoResponse {
        private Long id;
        private String cep;
        private String logradouro;
        private String complemento;
        private String numero;
        private String bairro;
        private String cidade;
        private String estado;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoClienteResponse {
        private Long id;
        private String cep;
        private String logradouro;
        private String complemento;
        private String numero;
        private String bairro;
        private String cidade;
        private String estado;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FormaPagamentoResponse {
        private String tipo;
        private String numeroCartao;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItensPedidoResponse {
        private Long id;
        private ProdutoResponse produto;
        private int quantidade;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProdutoResponse {
        private Long id;
        private String nome;
        private Float preco;
        private String descricao;
        private String urlImagem;
    }
    }
}
