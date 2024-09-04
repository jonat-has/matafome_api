package br.com.ifpe.matafome_api.modelo.pedido;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ifpe.matafome_api.api.pedido.PedidoRequest;
import br.com.ifpe.matafome_api.api.pedido.PedidoResponse;
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

    private final SimpMessagingTemplate messagingTemplate;

    public PedidoService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

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

        Forma_pagamento formaPagamento = formaPagamentoRepository.findById(pedidoRequest.getFormaPagamentoId())
            .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .empresa(empresa)
                .enderecoEntrega(enderecoEntrega)
                .formaPagamento(formaPagamento)
                .status(StatusPedidoEnum.PENDENTE)
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
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
            

                itensPedido.forEach(item -> {
                    item.setHabilitado(Boolean.TRUE);
                    item.setVersao(1L);
                    item.setDataCriacao(LocalDate.now());
                });

        pedido.setHabilitado(Boolean.TRUE);
        pedido.setVersao(1L);
        pedido.setDataCriacao(LocalDate.now());

        pedido.setItensPedido(itensPedido);

        // Calcular o valor total do pedido
        calcularValorTotal(pedido);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        messagingTemplate.convertAndSend("/topic/pedidoEmpresa/" + pedidoSalvo.getEmpresa().getId(), buildPedidoResponse(pedidoSalvo));
        return pedidoSalvo;
    }

    public PedidoResponse findById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    
        return buildPedidoResponse(pedido);
    }
    
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::buildPedidoResponse)
                .collect(Collectors.toList());
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

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        messagingTemplate.convertAndSend("/topic/pedidoEmpresa/" + pedidoSalvo.getEmpresa().getId(), buildPedidoResponse(pedidoSalvo));
        return pedidoSalvo;
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

    @Transactional
    public Pedido alterarStatusPagamento(Long pedidoId, StatusPagamentoEnum novoStatusPagamento) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Aplicar regras de negócios para transições de status de pagamento, se necessário
        pedido.setStatusPagamento(novoStatusPagamento);
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

    private PedidoResponse buildPedidoResponse(Pedido pedido) {
    return PedidoResponse.builder()
            .id(pedido.getId())
            .cliente(PedidoResponse.ClienteResponse.builder()
                    .id(pedido.getCliente().getId())
                    .nome(pedido.getCliente().getNome())
                    .foneCelular(pedido.getCliente().getFoneCelular())
                    .cpf(pedido.getCliente().getCpf())
                    .build())
            .empresa(PedidoResponse.EmpresaResponse.builder()
                    .id(pedido.getEmpresa().getId())
                    .nomeFantasia(pedido.getEmpresa().getNomeFantasia())
                    .horarioAbertura(pedido.getEmpresa().getHorarioAbertura())
                    .horarioFechamento(pedido.getEmpresa().getHorarioFechamento())
                    .tempoEntrega(pedido.getEmpresa().getTempoEntrega())
                    .imgCapa(pedido.getEmpresa().getImgCapa())
                    .imgPerfil(pedido.getEmpresa().getImgPerfil())
                   /* .categoria(pedido.getEmpresa().getCategoria())*/
                    .telefone(pedido.getEmpresa().getTelefone())
                    .taxaFrete(pedido.getEmpresa().getTaxaFrete())
                    .endereco(PedidoResponse.EnderecoResponse.builder()
                            .cep(pedido.getEmpresa().getEndereco().getCep())
                            .logradouro(pedido.getEmpresa().getEndereco().getLogradouro())
                            .complemento(pedido.getEmpresa().getEndereco().getComplemento())
                            .numero(pedido.getEmpresa().getEndereco().getNumero())
                            .bairro(pedido.getEmpresa().getEndereco().getBairro())
                            .cidade(pedido.getEmpresa().getEndereco().getCidade())
                            .estado(pedido.getEmpresa().getEndereco().getEstado())
                            .build())
                    .build())
            .enderecoEntrega(PedidoResponse.EnderecoClienteResponse.builder()
                    .id(pedido.getEnderecoEntrega().getId())
                    .cep(pedido.getEnderecoEntrega().getCep())
                    .logradouro(pedido.getEnderecoEntrega().getLogradouro())
                    .complemento(pedido.getEnderecoEntrega().getComplemento())
                    .numero(pedido.getEnderecoEntrega().getNumero())
                    .bairro(pedido.getEnderecoEntrega().getBairro())
                    .cidade(pedido.getEnderecoEntrega().getCidade())
                    .estado(pedido.getEnderecoEntrega().getEstado())
                    .build())
            .formaPagamento(PedidoResponse.FormaPagamentoResponse.builder()
                    .tipo(pedido.getFormaPagamento().getTipo())
                    .numeroCartao(pedido.getFormaPagamento().getNumero_cartao())
                    .build())
            .itensPedido(pedido.getItensPedido().stream()
                    .map(item -> PedidoResponse.ItensPedidoResponse.builder()
                            .id(item.getId())
                            .quantidade(item.getQuantidade())
                            .produto(PedidoResponse.ItensPedidoResponse.ProdutoResponse.builder()
                                    .id(item.getProduto().getId())
                                    .nome(item.getProduto().getNome())
                                    .preco(item.getProduto().getPreco())
                                    .descricao(item.getProduto().getDescricao())
                                    .urlImagem(item.getProduto().getUrlImagem())
                                    .build())
                            .build())
                    .collect(Collectors.toList()))
            .status(pedido.getStatus())
            .dataHoraPedido(pedido.getDataHoraPedido())
            .statusPagamento(pedido.getStatusPagamento())
            .taxaEntrega(pedido.getTaxaEntrega())
            .valorTotal(pedido.getValorTotal())
            .build();
}

        public List<PedidoResponse> findPedidosByEmpresaId(Long idEmpresa) {
                Empresa empresa = empresaRepository.findById(idEmpresa)
                        .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
                List<Pedido> pedidosDaEmpresa = empresa.getPedidos();
        
                // Transforma a lista de Pedido em uma lista de PedidoResponse
                return pedidosDaEmpresa.stream()
                        .map(this::buildPedidoResponse)
                        .collect(Collectors.toList());
        }

        public List<PedidoResponse> findPedidosByClienteId(Long idCliente) {
                Cliente cliente = clienteRepository.findById(idCliente)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrada"));
        
                List<Pedido> pedidosDoCliente = cliente.getPedidos();
        
                // Transforma a lista de Pedido em uma lista de PedidoResponse
                return pedidosDoCliente.stream()
                        .map(this::buildPedidoResponse)
                        .collect(Collectors.toList());
        }

}