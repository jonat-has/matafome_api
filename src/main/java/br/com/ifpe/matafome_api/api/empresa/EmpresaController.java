package br.com.ifpe.matafome_api.api.empresa;

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

import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import br.com.ifpe.matafome_api.modelo.empresa.Endereco_empresa;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin
public class EmpresaController {


    @Autowired
    private EmpresaService empresaService;

    /*End point de Empresa */
    /*ENDPOINT PUBLICO */
    @Operation(
       summary = "Serviço responsável por salvar uma empresa no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir uma empresa no sistema."
   )
    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) {

        Empresa empresa = request.build();

        Empresa empresaCriada = empresaService.save(empresa);
        return new ResponseEntity<Empresa>(empresaCriada, HttpStatus.CREATED);
    }


    /*ESSE ENDPOINT TEM Q SER ADMINONLY */
    @Operation(
        summary = "Serviço responsável por todas as empresas cadastradas no sistema.",
        description = "Exemplo de descrição de um endpoint retorna dados de empresas cadastrada no sistema."
    )
    @GetMapping
    public List<Empresa> listarTodos() {
        return empresaService.listarTodos();
    }   

    /*APENAS SE ESTIVER LOGADO PARA TER ACESSO A ESSE ENPOINT */
    @Operation(
        summary = "Serviço responsável por retorna dados de uma empresa cadastrada no sistema.",
        description = "Exemplo de descrição de um endpoint retorna dados de uma empresa cadastrada no sistema."
    )
    @GetMapping("/{id}")
    public Empresa obterPorID(@PathVariable Long id) {
        return empresaService.obterPorID(id);
    }   

    /*APENAS SE ESTIVER LOGADO PARA TER ACESSO A ESSE ENPOINT */
    @Operation(
        summary = "Serviço responsável por atualizar dados de uma empresa cadastrada no sistema.",
        description = "Exemplo de descrição de um endpoint atualizar dados de uma empresa cadastrada no sistema."
    )
    @PatchMapping("/{idEmpresa}")
    public ResponseEntity<Empresa> atualizarEmpresa( @PathVariable Long idEmpresa, @RequestBody @Valid AtualizacaoEmpresaRequest request) {

        Empresa empresaAtualizada = empresaService.atualizarEmpresa(idEmpresa, request);

        return ResponseEntity.ok(empresaAtualizada);
    }

   /*APENAS SE ESTIVER LOGADO PARA TER ACESSO A ESSE ENPOINT */
   @Operation(
    summary = "Serviço responsável por atualizar dados de uma empresa cadastrada no sistema.",
    description = "Exemplo de descrição de um endpoint atualizar dados de uma empresa cadastrada no sistema."
)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       empresaService.delete(id);
       return ResponseEntity.ok().build();
   }

   @Operation(
    summary = "Serviço responsável por retorna endereço de uma empresa cadastrada no sistema.",
    description = "Enviar id da empresa atraves da requisição para retorna o endereço dessa empresa."
)
   @GetMapping("/{idEmpresa}/endereco")
   public Empresa_enderecoResponse obterEmpresaComEndereco(@PathVariable Long idEmpresa) {
       return empresaService.obterEmpresaComEndereco(idEmpresa);
   }

    @PatchMapping("/{idEmpresa}/endereco")
    public ResponseEntity<Endereco_empresa> atualizarEndereco( @PathVariable Long idEmpresa, @RequestBody @Valid AtualizacaoEnderecoRequest request) {

        Endereco_empresa enderecoAtualizado = empresaService.atualizarEndereco_empresa(idEmpresa, request);
        return ResponseEntity.ok(enderecoAtualizado);
    }

   
}
