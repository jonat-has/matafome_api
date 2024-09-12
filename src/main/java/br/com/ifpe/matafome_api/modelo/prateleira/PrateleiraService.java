package br.com.ifpe.matafome_api.modelo.prateleira;

import java.util.List;
import java.util.Optional;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.prateleira.model_querysql.PrateleirasPromocionais;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private PrateleiraRepository prateleiraRepository;

    @Transactional
    public Prateleira save(Prateleira prateleira, Long empresaId,  Usuario usuarioLogado) {

        EntidadeAuditavelService.criarMetadadosEntidade(prateleira, usuarioLogado);

        Empresa empresa = empresaService.obterPorID(empresaId);

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
    public Prateleira update(Long id, Prateleira prateleiraAlterada, Usuario usuarioLogado) {
        Prateleira prateleira = this.obterPorID(id);

        prateleira.setNomePrateleira(prateleiraAlterada.getNomePrateleira());

        EntidadeAuditavelService.atualizarMetadadosEntidade(prateleira, usuarioLogado);

        return repository.save(prateleira);
    }

    @Transactional
    public Prateleira delete(Long id, Usuario usuarioLogado) {
        Prateleira prateleira = this.obterPorID(id);

        EntidadeAuditavelService.desativarEntidade(prateleira, usuarioLogado);

        return repository.save(prateleira);
    }

    public List<PrateleirasPromocionais> obeterPromo(Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by("nomePrateleira").ascending());

        return prateleiraRepository.findPrateleirasComNomesSemelhantes("promo", "oferta", "desconto", pageable);

    }
    
}