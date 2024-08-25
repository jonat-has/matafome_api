package br.com.ifpe.matafome_api.modelo.pedido;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ifpe.matafome_api.api.pedido.PedidoRequest;
import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.ClienteRepository;
import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;
import br.com.ifpe.matafome_api.modelo.cliente.Endereco_clienteRepository;
import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;
import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamentoRepository;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaRepository;
import br.com.ifpe.matafome_api.modelo.produto.ProdutoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private Endereco_clienteRepository enderecoClienteRepository;

    @Autowired
    private Forma_pagamentoRepository formaPagamentoRepository;

   // @Autowired
   // private Status_pedidoRepository statusPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Pedido save(PedidoRequest pedidoRequest) {

        Cliente cliente = clienteRepository.findById(pedidoRequest.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Empresa empresa = empresaRepository.findById(pedidoRequest.getEmpresaId())
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Endereco_cliente enderecoEntrega = enderecoClienteRepository.findById(pedidoRequest.getEnderecoEntregaId())
            .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        Forma_pagamento formaPagamento = formaPagamentoRepository.findById(pedidoRequest.getFormaPagamentoId())
            .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

       /*  Status_pedido statusPedido = statusPedidoRepository.findById(pedidoRequest.getStatusId())
            .orElseThrow(() -> new RuntimeException("Status padrão não encontrado"));*/

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .empresa(empresa)
                .enderecoEntrega(enderecoEntrega)
                .formaPagamento(formaPagamento)
               /*  .statusPedido(statusPedido)*/
                .statusPagamento(pedidoRequest.getStatusPagamento())
                .taxaEntrega(pedidoRequest.getTaxaEntrega())
                .dataHoraPedido(LocalDateTime.now())
                .build();

        List<Itens_pedido> itensPedido = pedidoRequest.getItens().stream().map(item -> {
            return Itens_pedido.builder()
                    .produto(produtoRepository.findById(item.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                    .pedido(pedido)
                    .quantidade(item.getQuantidade())
                    .valor(item.getValor())
                    .build();
        }).collect(Collectors.toList());

        pedido.setItensPedido(itensPedido);
        return pedidoRepository.save(pedido);
    }
}