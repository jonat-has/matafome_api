package br.com.ifpe.matafome_api.api.prateleira;


import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.modelo.prateleira.PrateleiraService;


@RestController
@RequestMapping("/api/empresas/{empresaId}/prateleiras")
@CrossOrigin
public class PrateleiraController {

    @Autowired
    private PrateleiraService prateleiraService;
    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    @Operation(summary = "Criar prateleira", description = "Cria uma nova prateleira para a empresa especificada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prateleira criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Prateleira.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                    content = @Content)
    })
    public ResponseEntity<Prateleira> criarPrateleira(@RequestBody PrateleiraRequest prateleiraRequest, @PathVariable Long empresaId, HttpServletRequest request) {

        Prateleira prateleiraCriada = prateleiraService.save(prateleiraRequest.build(), empresaId, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(prateleiraCriada);
    }

   /* @GetMapping
    public ResponseEntity<List<Prateleira>> listarTodos() {
        List<Prateleira> prateleiras = prateleiraService.listarTodos();
        
        return ResponseEntity.ok(prateleiras);
    }*/

    @GetMapping("/{id}")
    @Operation(summary = "Obter prateleira por ID", description = "Retorna os detalhes de uma prateleira específica pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prateleira encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Prateleira.class))),
            @ApiResponse(responseCode = "404", description = "Prateleira não encontrada",
                    content = @Content)
    })
    public ResponseEntity<Prateleira> obterPorID(@PathVariable Long id) {
        Prateleira prateleira = prateleiraService.obterPorID(id);
        return ResponseEntity.ok(prateleira);
    }


    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar prateleira", description = "Atualiza os detalhes de uma prateleira existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prateleira atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Prateleira.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Prateleira não encontrada",
                    content = @Content)
    })
    public ResponseEntity<Prateleira> atualizarPrateleira(@PathVariable Long id, @RequestBody Prateleira prateleiraAlterada,  HttpServletRequest request) {
        Prateleira prateleiraAtualizada = prateleiraService.update(id, prateleiraAlterada, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.ok(prateleiraAtualizada);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prateleira deletada com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Prateleira não encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado",
                    content = @Content)
    })
    public ResponseEntity<Void> deletarPrateleira(@PathVariable Long id, HttpServletRequest request) {
        prateleiraService.delete(id, usuarioService.obterUsuarioLogado(request));
        return ResponseEntity.noContent().build();
    }

}
