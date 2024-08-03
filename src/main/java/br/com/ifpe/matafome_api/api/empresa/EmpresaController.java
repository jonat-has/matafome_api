package br.com.ifpe.matafome_api.api.empresa;

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

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import br.com.ifpe.matafome_api.modelo.empresa.Endereco_empresa;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin
public class EmpresaController {


    @Autowired
    private EmpresaService empresaService;

    /*End point de Empresa */
    @Operation(
       summary = "Serviço responsável por salvar uma empresa no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir uma empresa no sistema."
   )
    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) {

        Empresa empresa = request.build();

        if (request.getPerfil() != null && !"".equals(request.getPerfil())) {

			if (request.getPerfil().equals("EMPRESA_USER")) {

				empresa.getUsuario().getRoles().add(Usuario.ROLE_EMPRESA_USER);

			} else if (request.getPerfil().equals("EMPRESA_ADMIN")) {

				empresa.getUsuario().getRoles().add(Usuario.ROLE_EMPRESA_ADMIN);
			}
		}


        Empresa empresaCriada = empresaService.save(empresa);
        return new ResponseEntity<Empresa>(empresaCriada, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Empresa> listarTodos() {
        return empresaService.listarTodos();
    }   

    @GetMapping("/{id}")
    public Empresa obterPorID(@PathVariable Long id) {
        return empresaService.obterPorID(id);
    }   

     @PutMapping("/{id}")
    public ResponseEntity<Empresa> update(@PathVariable("id") Long id, @RequestBody EmpresaRequest request) {   
       empresaService.update(id, request.build());
       return ResponseEntity.ok().build();
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

       empresaService.delete(id);
       return ResponseEntity.ok().build();
   }

   /*End point de Endereço de empresa */

   @PostMapping("/endereco/{empresaId}")
   public ResponseEntity<Endereco_empresa> adicionarEndereco_empresa(@PathVariable("empresaId") Long empresaId, @RequestBody @Valid Endereco_empresaRequest request) {

       Endereco_empresa endereco = empresaService.adicionarEndereco_empresa(empresaId, request.build());
       return new ResponseEntity<Endereco_empresa>(endereco, HttpStatus.CREATED);
   }

   @PutMapping("/endereco/{enderecoId}")
   public ResponseEntity<Endereco_empresa> atualizarEndereco_empresa(@PathVariable("enderecoId") Long enderecoId, @RequestBody Endereco_empresaRequest request) {

       Endereco_empresa endereco = empresaService.atualizarEndereco_empresa(enderecoId, request.build());
       return new ResponseEntity<Endereco_empresa>(endereco, HttpStatus.OK);
   }
  
   @DeleteMapping("/endereco/{enderecoId}")
   public ResponseEntity<Void> removerEndereco_empresa(@PathVariable("enderecoId") Long enderecoId) {

       empresaService.removerEndereco_empresa(enderecoId);
       return ResponseEntity.noContent().build();
   }


}
