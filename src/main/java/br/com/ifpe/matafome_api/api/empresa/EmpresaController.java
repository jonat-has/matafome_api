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

import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresa")
@CrossOrigin
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Operation(
       summary = "Serviço responsável por salvar uma empresa no sistema.",
       description = "Exemplo de descrição de um endpoint responsável por inserir uma empresa no sistema."
   )
    @PostMapping
    public ResponseEntity<Empresa> save(@RequestBody @Valid EmpresaRequest request) {

        Empresa empresa = empresaService.save(request.build());
        return new ResponseEntity<Empresa>(empresa, HttpStatus.CREATED);
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



}
