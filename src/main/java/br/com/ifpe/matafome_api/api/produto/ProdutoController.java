package br.com.ifpe.matafome_api.api.produto;


import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.produto.Adicionais;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifpe.matafome_api.modelo.produto.Produto;
import br.com.ifpe.matafome_api.modelo.produto.ProdutoService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/empresas/{empresaId}/prateleiras/{prateleiraId}/produtos")
@CrossOrigin
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Buscar todos os produtos", description = "Retorna uma lista paginada de produtos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public Page<Produto> findAllProdutos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return produtoService.findAllProdutos(page, size);
    }

    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterPorIDProdutos(@PathVariable Long id) {
        Produto produto = produtoService.obterPorID(id);
        return ResponseEntity.ok(produto);
    }

    @Operation(summary = "Adicionar novo produto", description = "Cria um novo produto dentro de uma prateleira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Produto> adicionarProduto(@PathVariable Long prateleiraId, @RequestBody @Valid Produto produto, HttpServletRequest request) {
        Produto novoProduto = produtoService.adicionarProduto(prateleiraId, produto, usuarioService.obterUsuarioLogado(request));
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar um produto", description = "Atualiza as informações de um produto pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produtoAlterado, HttpServletRequest request) {
        produtoService.update(id, produtoAlterado, usuarioService.obterUsuarioLogado(request));
        Produto produtoAtualizado = produtoService.obterPorID(id);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(summary = "Deletar um produto", description = "Exclui um produto pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id, HttpServletRequest request) {
        produtoService.delete(id, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar prateleira de um produto", description = "Move o produto para uma nova prateleira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prateleira atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto ou prateleira não encontrados")
    })
    @PatchMapping("/{produtoId}/prateleira/{novaPrateleiraId}")
    public ResponseEntity<Produto> atualizarPrateleiraDoProduto(@PathVariable Long produtoId, @PathVariable Long novaPrateleiraId, HttpServletRequest request) {
        Produto produtoAtualizado = produtoService.atualizarPrateleira(produtoId, novaPrateleiraId, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(summary = "Buscar produtos por nome", description = "Busca produtos pelo nome com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "Produtos não encontrados")
    })
    @GetMapping("/buscar")
    public Page<Produto> buscarPorNome(
            @RequestParam("nome") String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return produtoService.buscarPorNome(nome,  page, size);
    }

    @Operation(summary = "Buscar produtos por nome e prateleira", description = "Busca produtos por nome e prateleira com paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "404", description = "Produtos não encontrados")
    })
    @GetMapping("/buscarPorNomeEPrateleira")
    public Page<Produto> buscarPorNomeEPrateleira(
            @RequestParam("nome") String nome,
            @RequestParam("nomePrateleira") String nomePrateleira,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return produtoService.buscarPorNomeEPrateleira(nome, nomePrateleira, page, size);
    }

    @Operation(summary = "Adicionar adicionais a um produto", description = "Adiciona novos adicionais a um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adicionais adicionados com sucesso",
                    content = @Content(schema = @Schema(implementation = Adicionais.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PostMapping("/{produtoId}/adicionais")
    public  ResponseEntity<Adicionais> addAdicionais(@PathVariable Long produtoId, @RequestBody @Valid AdicionaisRequest adicionaisRequest, HttpServletRequest request) {

          
            Adicionais adicionais = adicionaisRequest.build();
     
            produtoService.adicionarAdicionais(produtoId, adicionais, usuarioService.obterUsuarioLogado(request));
            return ResponseEntity.ok(adicionais);
    }

    @Operation(summary = "Obter adicionais de todos os produtos", description = "Retorna todos os adicionais disponíveis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adicionais retornados com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "404", description = "Nenhum adicional encontrado")
    })
    @GetMapping("/adicionais")
    public ResponseEntity<List<Adicionais>> getAdicionais() {
        List<Adicionais> adicionais = produtoService.getAdicionais();
        return ResponseEntity.ok(adicionais);
    }

    @Operation(summary = "Obter adicionais de um produto", description = "Retorna todos os adicionais de um produto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adicionais retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{produtoId}/adicionais")
    public HashMap<String, Object> obter_todos_Adicionais(@PathVariable Long produtoId) {

        return produtoService.obterTodosAdicionaisProduto(produtoId);

    }


    @Operation(summary = "Atualizar adicionais de um produto", description = "Atualiza os adicionais de um produto pelo ID dos adicionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adicionais atualizados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto ou adicional não encontrado")
    })
    @PatchMapping("/{produtoId}/adicionais/{adicionaisId}")
    public ResponseEntity<Adicionais> atualizarAdicionais( @PathVariable Long adicionaisId, @RequestBody Adicionais adicionaisAlterado, HttpServletRequest request) {
        Adicionais atualizado = produtoService.atualizarAdicionais(adicionaisId, adicionaisAlterado, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Deletar adicionais de um produto", description = "Remove os adicionais de um produto pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Adicional removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto ou adicional não encontrado")
    })
    @DeleteMapping("/{produtoId}/adicionais/{adicionaisId}")
    public ResponseEntity<Void> deleteAdicionais(@PathVariable Long adicionaisId, HttpServletRequest request) {
        produtoService.deleteAdicionais(adicionaisId, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.noContent().build();
    }
}
