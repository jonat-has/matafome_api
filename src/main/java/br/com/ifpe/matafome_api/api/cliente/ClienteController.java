package br.com.ifpe.matafome_api.api.cliente;

import java.util.HashMap;
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
       description = "Endpoint responsável por inserir um cliente novo no sistema."
   )
    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody @Valid ClienteRequest clienteRequest, HttpServletRequest request) {

        System.out.println(clienteRequest.toString());
        Cliente cliente = clienteService.save(clienteRequest.build(), usuarioService.obterUsuarioLogado(request));
        return new ResponseEntity<Cliente>(cliente, HttpStatus.CREATED);

    }

    @Operation(
        summary = "Serviço responsável por listar todos os clientes no sistema.",
        description = "Endpoint responsável por listar todos os clientes no sistema."
    )
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }   

    @Operation(
       summary = "Serviço responsável por obter um cliente no sistema por ID.",
       description = "Endpoint responsável por obter um cliente no sistema ao receber ID do cliente."
   )

    @GetMapping("/{id}")
    public Cliente obterPorID(@PathVariable Long id) {
        return clienteService.obterPorID(id);
    }   

    @Operation(
       summary = "Serviço responsável por editar um cliente no sistema.",
       description = "Endpoint responsável por editar um cliente com base no ID fornecido."
   )

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable("id") Long id, @RequestBody ClienteRequest clienteRequest, HttpServletRequest request) {

	    clienteService.update(id, clienteRequest.build(), usuarioService.obterUsuarioLogado(request));
	    return ResponseEntity.ok().build();
    }

    @Operation(
       summary = "Serviço responsável por deletar um cliente no sistema.",
       description = "Endpoint responsável por deletar um cliente com base no ID fornecido."
   )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       clienteService.delete(id);
       return ResponseEntity.ok().build();
   }

   /*ENDPOINT DE ENDEREÇOS DE CLIENTE */

   @Operation(
       summary = "Serviço responsável por trazer todos os endereços de um cliente.",
       description = "Endpoint responsável por enviar objetos de tipo 'Cliente' e 'Endereco_cliente' registrados a patir do ID fornecido. A chave 'idCliente' contém o ID do cliente e a chave 'enderecos' contém todos os endereços do cliente."
   )

   @GetMapping("/endereco/{clienteId}")
   public HashMap<String, Object> obter_todos_osEnderecos(@PathVariable("clienteId") Long clienteId) {

       return clienteService.obterTodosEnderecosCliente(clienteId);
       
   }

   @Operation(
       summary = "Serviço responsável por incluir todos os endereços de um cliente.",
       description = "Endpoint responsável por criar uma entidade de tipo 'Endereco_cliente' a partir do ID fornecido."
   )

   @PostMapping("/endereco/{clienteId}")
   public ResponseEntity<Endereco_cliente> adicionarEndereco_cliente(@PathVariable("clienteId") Long clienteId, @RequestBody @Valid Endereco_clienteRequest request) {

       Endereco_cliente endereco = clienteService.adicionarEndereco_cliente(clienteId, request.build());
       return new ResponseEntity<Endereco_cliente>(endereco, HttpStatus.CREATED);
   }

   @Operation(
       summary = "Serviço responsável por editar todos os endereços de um cliente.",
       description = "Endpoint responsável por editar uma entidade de tipo 'Endereco_cliente' a partir do ID fornecido."
   )

   @PutMapping("/endereco/{enderecoId}")
   public ResponseEntity<Endereco_cliente> atualizarEndereco_cliente(@PathVariable("enderecoId") Long enderecoId, @RequestBody Endereco_clienteRequest request) {

       Endereco_cliente endereco = clienteService.atualizarEndereco_cliente(enderecoId, request.build());
       return new ResponseEntity<Endereco_cliente>(endereco, HttpStatus.OK);
   }

   @Operation(
       summary = "Serviço responsável por deletar todos os endereços de um cliente.",
       description = "Endpoint responsável por deletar uma entidade de tipo 'Endereco_cliente' a partir do ID fornecido."
   )
  
   @DeleteMapping("/endereco/{enderecoId}")
   public ResponseEntity<Void> removerEndereco_cliente(@PathVariable("enderecoId") Long enderecoId) {

       clienteService.removerEndereco_cliente(enderecoId);
       return ResponseEntity.noContent().build();
   }

   /*ENDPOINT DE FORMAS DE PAGAMENTO*/

   @Operation(
       summary = "Serviço responsável por trazer todos as formas de pagamento de um cliente.",
       description = "Endpoint responsável por trazer objetos de tipo 'Cliente' e 'Forma_pagamento' registrados a patir do ID fornecido. A chave 'idCliente' contém o ID do cliente e a chave 'pagtos' contém todos as formas de pagamento do cliente."
   )
   
   @GetMapping("/formasDePagamento/{clienteId}")
   public HashMap<String, Object> obter_todas_FormasPag(@PathVariable("clienteId") Long clienteId) {

       return clienteService.obterTodasFormasPagCliente(clienteId);
       
   }

   @Operation(
       summary = "Serviço responsável por incluir todas as formas de pagamento de um cliente.",
       description = "Endpoint responsável por inserir uma entidade de tipo 'Forma_pagamento' a partir do ID fornecido."
   )

   @PostMapping("/formasDePagamento/{clienteId}")
   public ResponseEntity<Forma_pagamento> adicionarFormaPagamento(@PathVariable("clienteId") Long clienteId, @RequestBody @Valid Forma_pagamentoRequest request) {

    Forma_pagamento formasDePagamento = clienteService.adicionarForma_pagamento(clienteId, request.build());
       return new ResponseEntity<Forma_pagamento>(formasDePagamento, HttpStatus.CREATED);
   }

   @Operation(
       summary = "Serviço responsável por editar todas as formas de pagamento de um cliente.",
       description = "Endpoint responsável por editar uma entidade de tipo 'Forma_pagamento' a partir do ID fornecido."
   )

   @PutMapping("/formasDePagamento/{formaId}")
   public ResponseEntity<Forma_pagamento> atualizarFormaPagamento(@PathVariable("formaId") Long formaId, @RequestBody Forma_pagamentoRequest request) {

    Forma_pagamento formasDePagamento = clienteService.atualizarForma_pagamento(formaId, request.build());
       return new ResponseEntity<Forma_pagamento>(formasDePagamento, HttpStatus.OK);
   }

   @Operation(
       summary = "Serviço responsável por deletar todas as formas de pagamento de um cliente.",
       description = "Endpoint responsável por deletar uma entidade de tipo 'Forma_pagamento' a partir do ID fornecido."
   )
  
   @DeleteMapping("/formasDePagamento/{formaId}")
   public ResponseEntity<Void> removerFormaPagamento(@PathVariable("formaId") Long formaId) {

       clienteService.removerForma_pagamento(formaId);
       return ResponseEntity.noContent().build();
   }


}
