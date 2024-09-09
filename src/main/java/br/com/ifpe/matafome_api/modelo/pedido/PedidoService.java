package br.com.ifpe.matafome_api.modelo.pedido;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import br.com.ifpe.matafome_api.api.pedido.HistoricoPedidosResponse;
import br.com.ifpe.matafome_api.api.pedido.HistoricoProdutosResponse;
import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.pedido.model_querysql.*;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Pedido save(PedidoRequest pedidoRequest,  Usuario usuarioLogado) {

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
                .observacao(pedidoRequest.getObservacao())
                .build();

                List<Itens_pedido> itensPedido = pedidoRequest.getItens().stream().map(item -> Itens_pedido.builder()
                        .produto(produtoRepository.findById(item.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                        .pedido(pedido)
                        .quantidade(item.getQuantidade())
                        .build()).collect(Collectors.toList());
            

                itensPedido.forEach(item -> {
                    item.setHabilitado(Boolean.TRUE);
                    item.setVersao(1L);
                    item.setDataCriacao(LocalDate.now());
                });

        EntidadeAuditavelService.criarMetadadosEntidade(pedido, usuarioLogado);

        pedido.setItensPedido(itensPedido);

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
    public Pedido update(Long id, PedidoRequest pedidoRequest, Usuario usuarioLogado) {
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
        pedido.setObservacao(pedidoRequest.getObservacao());

        List<Itens_pedido> itensPedido = pedidoRequest.getItens().stream().map(item -> Itens_pedido.builder()
                .produto(produtoRepository.findById(item.getProdutoId()).orElseThrow(() -> new RuntimeException("Produto não encontrado")))
                .pedido(pedido)
                .quantidade(item.getQuantidade())
                .build()).collect(Collectors.toList());
    
        pedido.setItensPedido(itensPedido);

        // Calcular o valor total do pedido
        calcularValorTotal(pedido);

        EntidadeAuditavelService.atualizarMetadadosEntidade(pedido, usuarioLogado);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        messagingTemplate.convertAndSend("/topic/pedidoEmpresa/" + pedidoSalvo.getEmpresa().getId(), buildPedidoResponse(pedidoSalvo));
        return pedidoSalvo;
    }


    @Transactional
    public void delete(Long id, Usuario usuarioLogado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        EntidadeAuditavelService.desativarEntidade(pedido, usuarioLogado);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido alterarStatus(Long pedidoId, StatusPedidoEnum novoStatus, Usuario usuarioLogado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        

        pedido.setStatus(novoStatus);

        EntidadeAuditavelService.atualizarMetadadosEntidade(pedido, usuarioLogado);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        messagingTemplate.convertAndSend("/topic/pedidoCliente/" + pedidoSalvo.getCliente().getId(), buildPedidoResponse(pedidoSalvo));
        return pedidoSalvo;
    }

    @Transactional
    public Pedido alterarStatusPagamento(Long pedidoId, StatusPagamentoEnum novoStatusPagamento, Usuario usuarioLogado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));


        pedido.setStatusPagamento(novoStatusPagamento);
        EntidadeAuditavelService.atualizarMetadadosEntidade(pedido, usuarioLogado);
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
                    .categoria(pedido.getEmpresa().getCategoria())
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
            .observacao(pedido.getObservacao())
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

   /* private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }*/

    public HistoricoPedidosResponse getHistoricoPedidos(Long empresaId, LocalDate startDate, LocalDate endDate) {
        // Total de vendas no período
        Double totalVendas = pedidoRepository.findTotalVendas(empresaId, startDate, endDate);

        // Quantidade de clientes únicos no período
        Long quantidadeClientes = pedidoRepository.countClientes(empresaId, startDate, endDate);

        // Quantidade total de pedidos no período
        Long quantidadePedidos = pedidoRepository.countPedidos(empresaId, startDate, endDate);

        // Data de hoje para calcular pedidos feitos hoje
        LocalDate hoje = LocalDate.now();
        Long pedidosHoje = pedidoRepository.countPedidosHoje(empresaId, hoje);

        LocalDate seteDiasAtras = hoje.minusDays(7);
        List<PedidosPorDia> pedidosUltimos7Dias = pedidoRepository.findPedidosUltimos7Dias(empresaId, seteDiasAtras, hoje);

        //Últimos 5 clientes
        Pageable limit = PageRequest.of(0, 5);
        List<UltimosClientes> ultimasVendas = pedidoRepository.findUltimasVendas(empresaId, limit);

        // Cria o response com os dados obtidos, tratando nulos com valores padrão
        return HistoricoPedidosResponse.builder()
                .totalVendas(totalVendas)
                .quantidadeClientes(quantidadeClientes)
                .quantidadePedidos(quantidadePedidos)
                .pedidosHoje(pedidosHoje != null ? pedidosHoje : 0)
                .pedidosUltimos7Dias(pedidosUltimos7Dias)
                .ultimasVendas(ultimasVendas)
                .build();
    }

    public HistoricoProdutosResponse getHistoricoProdutos(Long empresaId, LocalDate startDate, LocalDate endDate) {
        Pageable limit = PageRequest.of(0, 5);
        // Produto mais vendido
        List<ProdutosMaisVendidos> produtosMaisVendidos = pedidoRepository.findProdutoMaisVendido(empresaId, startDate, endDate, limit);

        // Produto mais lucrativo
        List<ProdutosMaisLucrativos> produtoMaisLucrativo = pedidoRepository.findProdutoMaisLucrativo(empresaId, startDate, endDate, limit);

        //Prateleira mais vendida
        List<PrateleirasMaisVendidas> prateleirasMaisVendidas = pedidoRepository.findPrateleiraMaisVendida(empresaId, startDate, endDate, limit);

        // Categoria mais lucrativa
        List<PrateleirasMaisLucrativas> prateleirasMaisLucrativas = pedidoRepository.findPrateleirasMaisLucrativas(empresaId, startDate, endDate, limit);

        return HistoricoProdutosResponse.builder()
                .produtosMaisVendidos(produtosMaisVendidos)
                .produtosMaisLucrativos(produtoMaisLucrativo)
                .prateleirasMaisVendidas(prateleirasMaisVendidas)
                .prateleirasMaisLucrativas(prateleirasMaisLucrativas)
                .build();
    }
}