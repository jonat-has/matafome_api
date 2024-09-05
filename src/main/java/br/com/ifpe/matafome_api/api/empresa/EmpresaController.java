package br.com.ifpe.matafome_api.api.empresa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.empresa.CategoriaEmpresaEnum;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private UsuarioService usuarioService;

    /*End point de Empresa */
    /*ENDPOINT PUBLICO */
    @Operation(
            summary = "Cadastro de uma nova empresa.",
            description = "Este endpoint permite a criação de uma nova empresa no sistema. Requer informações como razão social, nome fantasia, CNPJ, entre outros.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso", content = @Content(schema = @Schema(implementation = Empresa.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest empresaRequest, HttpServletRequest request) throws MessagingException {

        Empresa empresaCriada = empresaService.save(empresaRequest.build(), usuarioService.obterUsuarioLogado(request));
        return new ResponseEntity<>(empresaCriada, HttpStatus.CREATED);
    }


    /*ESSE ENDPOINT TEM Q SER ADMINONLY */
    @Operation(
            summary = "Listar todas as empresas cadastradas.",
            description = "Este endpoint retorna uma lista de todas as empresas cadastradas no sistema. Somente administradores podem acessar.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de empresas retornada com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Empresa.class)))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping
    public List<Empresa> listarTodos() {
        return empresaService.listarTodos();
    }   


    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
            summary = "Obter detalhes de uma empresa.",
            description = "Recupera os dados de uma empresa específica com base no seu ID. Requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso", content = @Content(schema = @Schema(implementation = Empresa.class))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/{id}")
    public Empresa obterPorID(@PathVariable Long id) {
        return empresaService.obterPorID(id);
    }   



    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
            summary = "Atualizar dados de uma empresa.",
            description = "Permite atualizar parcialmente as informações de uma empresa já cadastrada, como razão social, nome fantasia, etc. Requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso", content = @Content(schema = @Schema(implementation = Empresa.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PatchMapping("/{idEmpresa}")
    public ResponseEntity<Empresa> atualizarEmpresa( @PathVariable Long idEmpresa, @RequestBody @Valid AtualizacaoEmpresaRequest atualizacaoEmpresaRequest,  HttpServletRequest request) {

        Empresa empresaAtualizada = empresaService.atualizarEmpresa(idEmpresa, atualizacaoEmpresaRequest.build() , usuarioService.obterUsuarioLogado(request));

        return ResponseEntity.ok(empresaAtualizada);
    }

    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
            summary = "Excluir uma empresa do sistema.",
            description = "Remove uma empresa do sistema com base no seu ID. Requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Empresa excluída com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {

       empresaService.delete(id, usuarioService.obterUsuarioLogado(request));
       return ResponseEntity.ok().build();
   }



    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
            summary = "Obter endereço de uma empresa.",
            description = "Recupera o endereço de uma empresa específica usando seu ID. O endereço é retornado junto com os dados da empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço da empresa retornado com sucesso", content = @Content(schema = @Schema(implementation = Empresa_enderecoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
   @GetMapping("/{idEmpresa}/enderecos")
   public Empresa_enderecoResponse obterEmpresaComEndereco(@PathVariable Long idEmpresa) {
       return empresaService.obterEmpresaComEndereco(idEmpresa);
   }



    /* ENDPOINT RESTRITO - LOGIN NECESSÁRIO */
    @Operation(
            summary = "Atualizar o endereço de uma empresa.",
            description = "Permite atualizar parcialmente o endereço de uma empresa já cadastrada no sistema com base no ID da empresa. Requer autenticação.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso", content = @Content(schema = @Schema(implementation = Endereco_empresa.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @PatchMapping("/{idEmpresa}/enderecos")
    public ResponseEntity<Endereco_empresa> atualizarEndereco( @PathVariable Long idEmpresa, @RequestBody @Valid AtualizacaoEnderecoRequest atualizacaoEnderecoRequest, HttpServletRequest request) {

        Endereco_empresa enderecoAtualizado = empresaService.atualizarEndereco_empresa(idEmpresa, atualizacaoEnderecoRequest.build(), usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(enderecoAtualizado);
    }


    @Operation(
            summary = "Serviço responsável por trazer todas as prateleiras de uma empresa.",
            description = "Endpoint responsável por trazer objetos de tipo 'Empresa' e 'Prateleira' registrados a partir do ID fornecido da empresa. A chave 'idempresa' contém o ID da empresa e a chave 'prateleiras' contém todas as prateleiras de uma empresa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Prateleiras retornadas com sucesso", content = @Content(schema = @Schema(implementation = HashMap.class))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
   @GetMapping("/{empresaId}/prateleiras")
   public HashMap<String, Object> obter_TodasPrateleiras_Empresa(@PathVariable Long empresaId) {

       return empresaService.obterTodasPrateleirasEmpresa(empresaId);
       
   }


    @Operation( summary = "Busca empresas por nome fantasia",
                description = "Retorna uma lista paginada de empresas que correspondem ao nome fantasia fornecido.")
    @GetMapping("/buscarPorNomeFantasia")
   public ResponseEntity<Page<Empresa>> buscarPorNomeFantasia(
           @RequestParam String nome_fantasia,
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(empresaService.buscarPorNomeFantasia(nome_fantasia, page, size));
    }

    @Operation( summary = "Busca empresas por categoria(OBS:Esse endpoint e pra usar na barra de pesquisa)",
                description = "Retorna uma lista paginada de empresas que pertencem à categoria especificada.")
    @GetMapping("/buscarPorCategoria")
    public ResponseEntity<Page<Empresa>> buscarPorCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(empresaService.buscarPorCategoria(categoria, page, size));
    }

    @Operation( summary = "Filtrar empresas por categoria(OBS:Esse endpoint e para filtrar empresa pelos card de categoria)",
            description = "Retorna uma lista paginada de empresas que pertencem à categoria especificada.")
    @GetMapping("/filtrarPorCategoria")
    public ResponseEntity<Page<Empresa>> filtrarPorCategoria(
            @RequestParam String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(empresaService.filtrarPorCategoria(categoria, page, size));
    }

    @Operation( summary = "Busca empresas por nome fantasia e categoria",
                description = "Retorna uma lista paginada de empresas que correspondem ao nome fantasia e categoria fornecidos.")
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
                    @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PedidoResponse.class)))),
                    @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/{idEmpresa}/pedidos")
    public List<PedidoResponse> pedidosDaEmpresa(@PathVariable Long idEmpresa) {
       return pedidoService.findPedidosByEmpresaId(idEmpresa);
   }

    @Operation(
            summary = "Lista todas as categorias.",
            description = "Endpoint para recuperar todas as categorias cadastrada no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoriaEmpresaEnum.class)))),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    @GetMapping("/categorias")
    public Map<String, String> getCategorias() {
        return empresaService.getCategorias();
    }
}
