package br.com.ifpe.matafome_api.api.empresa;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.api.pedido.PedidoResponse;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import br.com.ifpe.matafome_api.modelo.empresa.Endereco_empresa;
import br.com.ifpe.matafome_api.modelo.pedido.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin
public class EmpresaController {


    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private PedidoService pedidoService;

    /*End point de Empresa */
    /*ENDPOINT PUBLICO */
    @Operation(
        summary = "Cadastro de uma nova empresa.",
        description = "Este endpoint permite a criação de uma nova empresa no sistema. Requer informações como razão social, nome fantasia, CNPJ, entre outros."
    )
    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) throws MessagingException {

        Empresa empresa = request.build();

        Empresa empresaCriada = empresaService.save(empresa);
        return new ResponseEntity<Empresa>(empresaCriada, HttpStatus.CREATED);
    }


    /*ESSE ENDPOINT TEM Q SER ADMINONLY */
    @Operation(
        summary = "Listar todas as empresas cadastradas.",
        description = "Este endpoint retorna uma lista de todas as empresas cadastradas no sistema. Somente administradores podem acessar."
    )
    @GetMapping
    public List<Empresa> listarTodos() {
        return empresaService.listarTodos();
    }   


    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
        summary = "Obter detalhes de uma empresa.",
        description = "Recupera os dados de uma empresa específica com base no seu ID. Requer autenticação."
    )
    @GetMapping("/{id}")
    public Empresa obterPorID(@PathVariable Long id) {
        return empresaService.obterPorID(id);
    }   

    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
        summary = "Atualizar dados de uma empresa.",
        description = "Permite atualizar parcialmente as informações de uma empresa já cadastrada, como razão social, nome fantasia, etc. Requer autenticação."
    )
    @PatchMapping("/{idEmpresa}")
    public ResponseEntity<Empresa> atualizarEmpresa( @PathVariable Long idEmpresa, @RequestBody @Valid AtualizacaoEmpresaRequest request) {

        Empresa empresaAtualizada = empresaService.atualizarEmpresa(idEmpresa, request);

        return ResponseEntity.ok(empresaAtualizada);
    }

    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
        summary = "Excluir uma empresa do sistema.",
        description = "Remove uma empresa do sistema com base no seu ID. Requer autenticação."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       empresaService.delete(id);
       return ResponseEntity.ok().build();
   }

    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
        summary = "Obter endereço de uma empresa.",
        description = "Recupera o endereço de uma empresa específica usando seu ID. O endereço é retornado junto com os dados da empresa."
    )
   @GetMapping("/{idEmpresa}/endereco")
   public Empresa_enderecoResponse obterEmpresaComEndereco(@PathVariable Long idEmpresa) {
       return empresaService.obterEmpresaComEndereco(idEmpresa);
   }

    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
        summary = "Atualizar o endereço de uma empresa.",
        description = "Permite atualizar parcialmente o endereço de uma empresa já cadastrada no sistema com base no ID da empresa. Requer autenticação."
    )
    @PatchMapping("/{idEmpresa}/endereco")
    public ResponseEntity<Endereco_empresa> atualizarEndereco( @PathVariable Long idEmpresa, @RequestBody @Valid AtualizacaoEnderecoRequest request) {

        Endereco_empresa enderecoAtualizado = empresaService.atualizarEndereco_empresa(idEmpresa, request);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    
   @Operation(
       summary = "Serviço responsável por trazer todos as prateleiras de uma empresa.",
       description = "Endpoint responsável por trazer objetos de tipo 'Empresa' e 'Prateleira' registrados a patir do ID fornecido da empresa. A chave 'idempresa' contém o ID da empresa e a chave 'prateleiras' contém todas as prateleiras de uma empresa."
   )
   
   @GetMapping("/{empresaId}/prateleiras")
   public HashMap<String, Object> obter_TodasPrateleiras_Empresa(@PathVariable("empresaId") Long empresaId) {

       return empresaService.obterTodasPrateleirasEmpresa(empresaId);
       
   }


     @GetMapping("/buscarPorNomeFantasia")
    public ResponseEntity<Page<Empresa>> buscarPorNomeFantasia(
            @RequestParam String nome_fantasia,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(empresaService.buscarPorNomeFantasia(nome_fantasia, page, size));
    }

    @GetMapping("/buscarPorCategoria")
    public ResponseEntity<Page<Empresa>> buscarPorCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(empresaService.buscarPorCategoria(categoria, page, size));
    }

    @GetMapping("/buscarPorNomeFantasiaECategoria")
    public ResponseEntity<Page<Empresa>> buscarPorNomeFantasiaECategoria(
            @RequestParam String nome_fantasia,
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(empresaService.buscarPorNomeFantasiaECategoria(nome_fantasia, categoria, page, size));
    }



    @Operation(
    summary = "Lista todos os pedidos de uma empresa.",
    description = "Endpoint para recuperar todos os pedidos associados a uma empresa específica.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso", content = @Content(schema = @Schema(implementation = HashMap.class))),
        @ApiResponse(responseCode = "404", description = "Empresa não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{idEmpresa}/pedidos")
    public List<PedidoResponse> pedidosDaEmpresa(@PathVariable Long idEmpresa) {
       return pedidoService.findPedidosByEmpresaId(idEmpresa);
   }

   
}
