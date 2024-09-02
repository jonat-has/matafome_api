package br.com.ifpe.matafome_api.api.pedido;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import br.com.ifpe.matafome_api.modelo.pedido.Pedido;

@Controller
public class PedidoControllerSock {

    @MessageMapping("/pedidos/{empresaId}")
    @SendTo("/topic/pedidos/{empresaId}")
    public String enviarPedido(@DestinationVariable String empresaId, Pedido pedido) {
        // Jonathas, aqui é para configurar as mensagens que serão enviadas para o websocket para que o cliente possa receber.
        // Essas mensagens serão as atualizações de status do pedido.
        System.out.println("Enviado!");
        return "Enviado!";
    }
}