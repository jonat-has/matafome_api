package br.com.ifpe.matafome_api.modelo.prateleira;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class PrateleiraService {

    @Autowired
    private PrateleiraRepository repository;

    @Autowired
    private EmpresaService empresaService;

    @Transactional
    public Prateleira save(Prateleira prateleira, Long empresaId) {

        prateleira.setHabilitado(Boolean.TRUE);
        prateleira.setVersao(1L);
        prateleira.setDataCriacao(LocalDate.now());
           // Obter a empresa pelo ID
        Empresa empresa = empresaService.obterPorID(empresaId);

        // Associar a prateleira à empresa
        prateleira.setEmpresa(empresa);

        return repository.save(prateleira);

    }

    public List<Prateleira> listarTodos() {
        return repository.findAll();
    }

    public Prateleira obterPorID(Long id) {

        Optional<Prateleira> consulta = repository.findById(id);

        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("prateleira", id);
        }
        
    }

    @Transactional
    public void update(Long id, Prateleira prateleiraAlterada) {
        Prateleira prateleira = repository.findById(id).get();
        prateleira.setNome_prateleira(prateleiraAlterada.getNome_prateleira());
        prateleira.setVersao(prateleira.getVersao() + 1);
        repository.save(prateleira);
    }

    @Transactional
    public void delete(Long id) {
        Prateleira prateleira = repository.findById(id).get();
        prateleira.setHabilitado(Boolean.FALSE);
        prateleira.setVersao(prateleira.getVersao() + 1);
        repository.save(prateleira);
    }
    
}
