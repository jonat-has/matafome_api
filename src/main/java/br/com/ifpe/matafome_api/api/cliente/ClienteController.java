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

import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.ClienteService;
import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;
import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cliente")
@CrossOrigin
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

        @Autowired
    private UsuarioService usuarioService;

    /*ENDPOINT DE CLIENTE */
    @Operation(
       summary = "Serviço responsável por salvar um cliente no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir um cliente no sistema."
   )
    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody @Valid ClienteRequest clienteRequest, HttpServletRequest request) {

        Cliente cliente = clienteService.save(clienteRequest.build(), usuarioService.obterUsuarioLogado(request));
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
    public ResponseEntity<Cliente> update(@PathVariable("id") Long id, @RequestBody ClienteRequest clienteRequest, HttpServletRequest request) {

	    clienteService.update(id, clienteRequest.build(), usuarioService.obterUsuarioLogado(request));
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

   /*ENDPOINT DE FORMAS DE PAGAMENTO*/
   
   @PostMapping("/formasDePagamento/{clienteId}")
   public ResponseEntity<Forma_pagamento> adicionarFormaPagamento(@PathVariable("clienteId") Long clienteId, @RequestBody @Valid Forma_pagamentoRequest request) {

    Forma_pagamento formasDePagamento = clienteService.adicionarForma_pagamento(clienteId, request.build());
       return new ResponseEntity<Forma_pagamento>(formasDePagamento, HttpStatus.CREATED);
   }

   @PutMapping("/formasDePagamento/{formaId}")
   public ResponseEntity<Forma_pagamento> atualizarFormaPagamento(@PathVariable("formaId") Long formaId, @RequestBody Forma_pagamentoRequest request) {

    Forma_pagamento formasDePagamento = clienteService.atualizarForma_pagamento(formaId, request.build());
       return new ResponseEntity<Forma_pagamento>(formasDePagamento, HttpStatus.OK);
   }
  
   @DeleteMapping("/formasDePagamento/{formaId}")
   public ResponseEntity<Void> removerFormaPagamento(@PathVariable("formaId") Long formaId) {

       clienteService.removerForma_pagamento(formaId);
       return ResponseEntity.noContent().build();
   }


}
