package br.com.ifpe.matafome_api.api.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.ClienteService;
import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
@CrossOrigin
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    /*ENDPOINT DE CLIENTE */
    @Operation(
       summary = "Serviço responsável por salvar um cliente no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir um cliente no sistema."
   )
    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody @Valid ClienteRequest request) {

        Cliente cliente = clienteService.save(request.build());
        return new ResponseEntity<Cliente>(cliente, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }   

    @GetMapping("/{id}")
    public Cliente obterPorID(@PathVariable Long id) {
        return clienteService.obterPorID(id);
    }   

     @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable("id") Long id, @RequestBody ClienteRequest request) {   
       clienteService.update(id, request.build());
       return ResponseEntity.ok().build();
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       clienteService.delete(id);
       return ResponseEntity.ok().build();
   }

   /*ENDPOINT DE ENDEREÇOS DE CLIENTE */
   @PostMapping("/endereco/{clienteId}")
   public ResponseEntity<Endereco_cliente> adicionarEndereco_cliente(@PathVariable("clienteId") Long clienteId, @RequestBody @Valid Endereco_clienteRequest request) {

       Endereco_cliente endereco = clienteService.adicionarEndereco_cliente(clienteId, request.build());
       return new ResponseEntity<Endereco_cliente>(endereco, HttpStatus.CREATED);
   }

   @PutMapping("/endereco/{enderecoId}")
   public ResponseEntity<Endereco_cliente> atualizarEndereco_cliente(@PathVariable("enderecoId") Long enderecoId, @RequestBody Endereco_clienteRequest request) {

       Endereco_cliente endereco = clienteService.atualizarEndereco_cliente(enderecoId, request.build());
       return new ResponseEntity<Endereco_cliente>(endereco, HttpStatus.OK);
   }
  
   @DeleteMapping("/endereco/{enderecoId}")
   public ResponseEntity<Void> removerEndereco_cliente(@PathVariable("enderecoId") Long enderecoId) {

       clienteService.removerEndereco_cliente(enderecoId);
       return ResponseEntity.noContent().build();
   }




}
