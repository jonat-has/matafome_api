package br.com.ifpe.matafome_api.api.prateleira;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.modelo.prateleira.PrateleiraService;

@RestController
@RequestMapping("/api/prateleira")
public class PrateleiraController {

    @Autowired
    private PrateleiraService prateleiraService;

    @PostMapping
    public ResponseEntity<Prateleira> criarPrateleira(@RequestBody Prateleira prateleira) {
        Prateleira prateleiraCriada = prateleiraService.save(prateleira);
        return ResponseEntity.ok(prateleiraCriada);
    }

    @GetMapping
    public ResponseEntity<List<Prateleira>> listarTodos() {
        List<Prateleira> prateleiras = prateleiraService.listarTodos();
        return ResponseEntity.ok(prateleiras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prateleira> obterPorID(@PathVariable Long id) {
        Prateleira prateleira = prateleiraService.obterPorID(id);
        return ResponseEntity.ok(prateleira);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prateleira> atualizarPrateleira(@PathVariable Long id,
            @RequestBody Prateleira prateleiraAlterada) {
        prateleiraService.update(id, prateleiraAlterada);
        Prateleira prateleiraAtualizada = prateleiraService.obterPorID(id);
        return ResponseEntity.ok(prateleiraAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPrateleira(@PathVariable Long id) {
        prateleiraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
