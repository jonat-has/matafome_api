package br.com.ifpe.matafome_api.api.cliente;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.ClienteService;
import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;
import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;
import br.com.ifpe.matafome_api.modelo.pedido.Pedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/clientes")
@CrossOrigin
public class ClienteController {


    @Autowired
    private ClienteService clienteService;

    @Autowired
    private UsuarioService usuarioService;

    /*ENDPOINT DE CLIENTE */
    @Operation(
        summary = "Salva um novo cliente no sistema.",
        description = "Endpoint para criar um novo cliente. Requer um objeto JSON com os detalhes do cliente no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso", content = @Content(schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    @PostMapping
    public ResponseEntity<Cliente> save(@RequestBody @Valid ClienteRequest clienteRequest, HttpServletRequest request) throws MessagingException {
        Cliente cliente = clienteService.save(clienteRequest.build(), usuarioService.obterUsuarioLogado(request));
        return new ResponseEntity<Cliente>(cliente, HttpStatus.CREATED);
    }



    @Operation(
        summary = "Lista todos os clientes.",
        description = "Endpoint para listar todos os clientes cadastrados no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Cliente.class)))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }   



    
    @Operation(
        summary = "Obter um cliente por ID.",
        description = "Endpoint para recuperar os detalhes de um cliente específico pelo seu ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso", content = @Content(schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    @GetMapping("/{id}")
    public Cliente obterPorID(@PathVariable Long id) {
        return clienteService.obterPorID(id);
    }   




    @Operation(
        summary = "Edita um cliente existente.",
        description = "Endpoint para editar os detalhes de um cliente já existente no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente editado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody ClienteRequest clienteRequest, HttpServletRequest request) {

	    Cliente clienteAtt = clienteService.update(id, clienteRequest.build(), usuarioService.obterUsuarioLogado(request));
	    return new ResponseEntity<Cliente>(clienteAtt, HttpStatus.OK);
    }




    @Operation(
        summary = "Deleta um cliente.",
        description = "Endpoint para deletar um cliente específico do sistema pelo ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       clienteService.delete(id);
       return ResponseEntity.ok().build();
   }




   /*ENDPOINT DE ENDEREÇOS DE CLIENTE */
    @Operation(
        summary = "Lista todos os endereços de um cliente.",
        description = "Endpoint para recuperar todos os endereços associados a um cliente específico.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Endereços do cliente retornados com sucesso", content = @Content(schema = @Schema(implementation = HashMap.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @GetMapping("/{clienteId}/enderecos")
   public HashMap<String, Object> obter_todos_osEnderecos(@PathVariable Long clienteId) {

       return clienteService.obterTodosEnderecosCliente(clienteId);
       
   }




    @Operation(
        summary = "Adiciona um endereço novo a um cliente no sistema.",
        description = "Endpoint para criar um novo endereço. Requer um objeto JSON com os detalhes do endereçp no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso", content = @Content(schema = @Schema(implementation = Endereco_cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @PostMapping("/{clienteId}/enderecos")
   public ResponseEntity<Endereco_cliente> adicionarEndereco_cliente(@PathVariable Long clienteId, @RequestBody @Valid Endereco_clienteRequest request) {
       Endereco_cliente endereco = clienteService.adicionarEndereco_cliente(clienteId, request.build());
       return new ResponseEntity<Endereco_cliente>(endereco, HttpStatus.CREATED);
   }




    @Operation(
        summary = "Edita um endereço existente.",
        description = "Endpoint para editar os detalhes de um endereço já existente no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Endereço editado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @PatchMapping("/{clienteId}/enderecos/{enderecoId}")
   public ResponseEntity<Endereco_cliente> atualizarEndereco_cliente(@PathVariable Long enderecoId, @RequestBody Endereco_clienteRequest request) {

       Endereco_cliente endereco = clienteService.atualizarEndereco_cliente(enderecoId, request.build());
       return new ResponseEntity<Endereco_cliente>(endereco, HttpStatus.OK);
   }



    @Operation(
        summary = "Deleta um endereço.",
        description = "Endpoint para deletar um endereço específico do sistema pelo ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Endereço deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @DeleteMapping("/enderecos/{enderecoId}")
   public ResponseEntity<Void> removerEndereco_cliente(@PathVariable Long enderecoId) {

       clienteService.removerEndereco_cliente(enderecoId);
       return ResponseEntity.noContent().build();
   }






   /*ENDPOINT DE FORMAS DE PAGAMENTO*/
   @Operation(
    summary = "Lista todos as formas de pagamento de um cliente.",
    description = "Endpoint para recuperar todos as formas de pagamento associados a um cliente específico.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Formas de pagamento retornados com sucesso", content = @Content(schema = @Schema(implementation = HashMap.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    }
    )
   @GetMapping("/{clienteId}/formasDePagamentos")
   public HashMap<String, Object> obter_todas_FormasPag(@PathVariable Long clienteId) {

       return clienteService.obterTodasFormasPagCliente(clienteId);
       
   }



    @Operation(
        summary = "Adiciona uma forma de pagamento nova a um cliente no sistema.",
        description = "Endpoint para criar uma forma de pagamento. Requer um objeto JSON com os detalhes da formas de pagamento no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Formas de pagamento criado com sucesso", content = @Content(schema = @Schema(implementation = Endereco_cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @PostMapping("/{clienteId}/formasDePagamentos")
   public ResponseEntity<Forma_pagamento> adicionarFormaPagamento(@PathVariable Long clienteId, @RequestBody @Valid Forma_pagamentoRequest request) {

    Forma_pagamento formasDePagamento = clienteService.adicionarForma_pagamento(clienteId, request.build());
       return new ResponseEntity<Forma_pagamento>(formasDePagamento, HttpStatus.CREATED);
   }




    @Operation(
        summary = "Edita uma Forma de pagamento existente.",
        description = "Endpoint para editar os detalhes de uma Forma de pagamento já existente no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento editado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @PatchMapping("/{clientesId}/formasDePagamentos/{formaId}")
   public ResponseEntity<Forma_pagamento> atualizarFormaPagamento(@PathVariable Long formaId, @RequestBody Forma_pagamentoRequest request) {

    Forma_pagamento formasDePagamento = clienteService.atualizarForma_pagamento(formaId, request.build());
       return new ResponseEntity<Forma_pagamento>(formasDePagamento, HttpStatus.OK);
   }





    @Operation(
        summary = "Deleta uma Forma de pagamento.",
        description = "Endpoint para deletar uma Forma de pagamento específico do sistema pelo ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Forma de pagamento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Forma de pagamento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
        }
    )
   @DeleteMapping("/{clientesId}/formasDePagamentos/{formaId}")
   public ResponseEntity<Void> removerFormaPagamento(@PathVariable Long formaId) {

       clienteService.removerForma_pagamento(formaId);
       return ResponseEntity.noContent().build();
   }




   @Operation(
    summary = "Lista todos os pedidos de um cliente.",
    description = "Endpoint para recuperar todos os pedidos associados a um cliente específico.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso", content = @Content(schema = @Schema(implementation = HashMap.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    }
    )
   @GetMapping("/{idCliente}/pedidos")
   public List<Pedido> pedidosDoCliente(@PathVariable Long idCliente) {
       return clienteService.pedidoCliente(idCliente);
   }
   


}
