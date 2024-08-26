package br.com.ifpe.matafome_api.api.pedido;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<Pedido> findAll() {
        return pedidoService.listarTodos();
    }
    
    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoRequest pedidoRequest) {
        Pedido pedido = pedidoService.save(pedidoRequest);
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestBody StatusPedidoEnum novoStatus) {
        Pedido pedidoAtualizado = pedidoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(pedidoAtualizado);
    }

     @GetMapping("/{id}")
    public ResponseEntity<Pedido> obterPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody PedidoRequest pedidoRequest) {
        Pedido pedidoAtualizado = pedidoService.update(id, pedidoRequest);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPedido(@PathVariable Long id) {
        pedidoService.delete(id);
    }

}