package br.com.ifpe.matafome_api.api.pedido;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.pedido.StatusPagamentoEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import br.com.ifpe.matafome_api.modelo.pedido.Pedido;
import br.com.ifpe.matafome_api.modelo.pedido.PedidoService;
import br.com.ifpe.matafome_api.modelo.pedido.StatusPedidoEnum;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Buscar todos os pedidos", description = "Retorna uma lista de todos os pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public List<PedidoResponse> findAll() {
        return pedidoService.listarTodos();
    }


    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido a partir dos dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoRequest pedidoRequest, HttpServletRequest request) {
        Pedido pedido = pedidoService.save(pedidoRequest, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(pedido);
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/statusPedido")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestBody StatusRequest novoStatus, HttpServletRequest request) {
        Pedido pedidoAtualizado = pedidoService.alterarStatus(id, novoStatus.getNovoStatusPedido(), usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Atualizar status de pagamento do pedido", description = "Atualiza o status de pagamento de um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de pagamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/statusPagamento")
    public ResponseEntity<Pedido> atualizarStatusPagamento(@PathVariable Long id, @RequestBody StatusPagamentoRequest novoStatus, HttpServletRequest request) {
        Pedido pedidoAtualizado = pedidoService.alterarStatusPagamento(id, novoStatus.getNovoStatusPagamento(), usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Obter pedido por ID", description = "Retorna um pedido pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obterPedidoPorId(@PathVariable Long id) {
        PedidoResponse pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    @Operation(summary = "Atualizar pedido", description = "Atualiza um pedido existente com novos dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody PedidoRequest pedidoRequest, HttpServletRequest request) {
        Pedido pedidoAtualizado = pedidoService.update(id, pedidoRequest, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Deletar pedido", description = "Exclui um pedido pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPedido(@PathVariable Long id, HttpServletRequest request) {

        pedidoService.delete(id, usuarioService.obterUsuarioLogado(request));
    }

    @GetMapping("/historicoPedidos")
    public ResponseEntity<HistoricoPedidosResponse> getHistoricoPedidos(
            @RequestParam Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        HistoricoPedidosResponse historico = pedidoService.getHistoricoPedidos(empresaId, startDate, endDate);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/historicoProdutos")
    public ResponseEntity<HistoricoProdutosResponse> getHistoricoProdutos(
            @RequestParam Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        HistoricoProdutosResponse historico = pedidoService.getHistoricoProdutos(empresaId, startDate, endDate);
        return ResponseEntity.ok(historico);
    }

    @GetMapping("/historicoClientes")
    public ResponseEntity<HistoricoClientesResponse> getHistoricoClientes(
            @RequestParam Long empresaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        HistoricoClientesResponse historico = pedidoService.getHistoricoClientes(empresaId, startDate, endDate);
        return ResponseEntity.ok(historico);
    }

}