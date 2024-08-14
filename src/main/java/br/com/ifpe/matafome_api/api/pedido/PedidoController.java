package br.com.ifpe.matafome_api.api.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifpe.matafome_api.modelo.pedido.Pedido;
import br.com.ifpe.matafome_api.modelo.pedido.PedidoService;



@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoRequest pedidoRequest) {
        Pedido pedido = pedidoService.save(pedidoRequest);
        return ResponseEntity.ok(pedido);
    }
}