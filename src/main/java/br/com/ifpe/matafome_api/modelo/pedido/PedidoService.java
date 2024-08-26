package br.com.ifpe.matafome_api.modelo.pedido;


import java.time.LocalDate;
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

        Forma_pagamento formaPagamento = formaPagamentoRepository.findById((long) 1)
            .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .empresa(empresa)
                .enderecoEntrega(enderecoEntrega)
                .formaPagamento(formaPagamento)
                .status(StatusPedidoEnum.PENDENTE)
                .statusPagamento(pedidoRequest.getStatusPagamento())
                .taxaEntrega(pedidoRequest.getTaxaEntrega())
                .dataHoraPedido(LocalDateTime.now())
                .build();
                List<Itens_pedido> itensPedido = pedidoRequest.getItens().stream().map(item -> {
                    return Itens_pedido.builder()
                            .produto(produtoRepository.findById(item.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                            .pedido(pedido)
                            .quantidade(item.getQuantidade())
                            .build();
                }).collect(Collectors.toList());

        pedido.setHabilitado(Boolean.TRUE);
        pedido.setVersao(1L);
        pedido.setDataCriacao(LocalDate.now());

        pedido.setItensPedido(itensPedido);

        // Calcular o valor total do pedido
        calcularValorTotal(pedido);

        return pedidoRepository.save(pedido);
    }

    public Pedido findById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
    
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido update(Long id, PedidoRequest pedidoRequest) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Cliente cliente = clienteRepository.findById(pedidoRequest.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Empresa empresa = empresaRepository.findById(pedidoRequest.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        Endereco_cliente enderecoEntrega = enderecoClienteRepository.findById(pedidoRequest.getEnderecoEntregaId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        Forma_pagamento formaPagamento = formaPagamentoRepository.findById(pedidoRequest.getFormaPagamentoId())
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        pedido.setCliente(cliente);
        pedido.setEmpresa(empresa);
        pedido.setEnderecoEntrega(enderecoEntrega);
        pedido.setFormaPagamento(formaPagamento);
        pedido.setStatusPagamento(pedidoRequest.getStatusPagamento());
        pedido.setTaxaEntrega(pedidoRequest.getTaxaEntrega());

        List<Itens_pedido> itensPedido = pedidoRequest.getItens().stream().map(item -> {
            return Itens_pedido.builder()
                    .produto(produtoRepository.findById(item.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                    .pedido(pedido)
                    .quantidade(item.getQuantidade())
                    .build();
        }).collect(Collectors.toList());

        pedido.setItensPedido(itensPedido);

        // Calcular o valor total do pedido
        calcularValorTotal(pedido);

        return pedidoRepository.save(pedido);
    }


    @Transactional
    public void delete(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedidoRepository.delete(pedido);
    }

    @Transactional
    public Pedido alterarStatus(Long pedidoId, StatusPedidoEnum novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        // Aplicar regras de negócios para transições de status se necessário
        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    private void calcularValorTotal(Pedido pedido) {
        if (pedido.getItensPedido() != null) {
            Double valorTotal = pedido.getItensPedido().stream()
                .mapToDouble(item -> item.getProduto().getPreco() * item.getQuantidade())
                .sum();
            pedido.setValorTotal(valorTotal);
        } else {
            pedido.setValorTotal(0.0);
        }
    }
}