package br.com.ifpe.matafome_api.api.websocket;

import br.com.ifpe.matafome_api.api.pedido.PedidoResponse;
import br.com.ifpe.matafome_api.modelo.pedido.PedidoService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


import java.util.List;

@Controller
public class PedidoWebSocketController {

    private final PedidoService pedidoService;

    public PedidoWebSocketController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @MessageMapping("/api/pedidos")
    @SendTo("/topic/pedidoEmpresa/{idEmpresa}")
    public List<PedidoResponse> pedidosDaEmpresa(@DestinationVariable Long idEmpresa) {
        return pedidoService.findPedidosByEmpresaId(idEmpresa);
    }

    @MessageMapping("/api/pedidos/{id}/statusPedido")
    @SendTo("/topic/pedidoCliente/{idCliente}")
    public List<PedidoResponse> pedidosDoCliente(@DestinationVariable Long idCliente) {
        return pedidoService.findPedidosByClienteId(idCliente);
    }
}
