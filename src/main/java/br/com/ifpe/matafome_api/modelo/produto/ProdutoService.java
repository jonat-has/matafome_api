package br.com.ifpe.matafome_api.modelo.produto;

import java.beans.PropertyDescriptor;
import java.util.*;


import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.empresa.EmpresaService;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.modelo.prateleira.PrateleiraRepository;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PrateleiraRepository prateleiraRepository;


    @Autowired
    private AdicionaisRepository adicionaisRepository;
    @Autowired
    private EmpresaService empresaService;


    @Transactional
    public Produto adicionarProduto(Long obterPorIDPrateleira, Produto produto, Usuario usuarioLogado) {
        Prateleira prateleira = this.obterPorIDPrateleira(obterPorIDPrateleira);

        EntidadeAuditavelService.criarMetadadosEntidade(produto, usuarioLogado);
        produto.setPrateleira(prateleira);

        return produtoRepository.save(produto);
    }


    @Transactional
    public Produto atualizarPrateleira(Long produtoId, Long novaPrateleiraId, Usuario usuarioLogado) {
        Produto produto = this.obterPorID(produtoId);
        Prateleira novaPrateleira = this.obterPorIDPrateleira(novaPrateleiraId);

        EntidadeAuditavelService.atualizarMetadadosEntidade(produto, usuarioLogado);
        EntidadeAuditavelService.atualizarMetadadosEntidade(novaPrateleira, usuarioLogado);

        produto.setPrateleira(novaPrateleira);

        return produtoRepository.save(produto);
    }

    public Page<Produto> findAllProdutos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.findAll(pageable);
    }

    public Produto obterPorID(Long id) {
        Optional<Produto> consulta = produtoRepository.findById(id);

        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("produto", id);
        }
    }

    public HashMap<String, Object> obterProdutoEmpresa(Long id) {
        Produto produto = this.obterPorID(id);
        Long prateleiraId = produto.getPrateleira().getId();
        Long empresaId = produto.getPrateleira().getEmpresa().getId();

        HashMap<String, Object> produtoResult = new HashMap<>();

        produtoResult.put("idEmpresa", empresaId);
        produtoResult.put("idPrateleira", prateleiraId);
        produtoResult.put("Produto", produto);

        return produtoResult;
    }

    public Prateleira obterPorIDPrateleira(Long id) {
        Optional<Prateleira> consulta = prateleiraRepository.findById(id);
        if (consulta.isPresent()) {
            return consulta.get();
        } else {
            throw new EntidadeNaoEncontradaException("prateleira", id);
        }
    }

    @Transactional
    public Produto update(Long id, Produto produtoAlterado, Usuario usuarioLogado) {
        Produto produto = obterPorID(id);

        EntidadeAuditavelService.atualizarMetadadosEntidade(produto, usuarioLogado );

        String[] ignoreProperties = getNullPropertyNames(produtoAlterado);
        List<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("adicionais");
        ignoreList.add("prateleira");

        BeanUtils.copyProperties(produtoAlterado, produto, ignoreList.toArray(new String[0]));

        return produtoRepository.save(produto);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Transactional
    public void delete(Long id, Usuario usuarioLogado) {
        Produto produto = obterPorID(id);

        EntidadeAuditavelService.desativarEntidade(produto, usuarioLogado);

        produtoRepository.save(produto);
    }

    public Page<Produto> buscarPorNome(String nome, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.findByNome(nome, pageable);
    }

    public Page<Produto> buscarPorNomeEPrateleira(String nome, String nomePrateleira, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return produtoRepository.findByNomeAndPrateleira(nome, nomePrateleira, pageable);
    }

    /* FUNÇÔES DE ADICIONAIS*/

    public List<Adicionais> getAdicionais() {
        return adicionaisRepository.findAll();
    }

    @Transactional
    public Adicionais adicionarAdicionais(Long produtoId, Adicionais adicionais, Usuario usuarioLogado) {

        Produto produto = obterPorID(produtoId);

        EntidadeAuditavelService.criarMetadadosEntidade(adicionais, usuarioLogado);

        adicionais.setProduto(produto);

        return adicionaisRepository.save(adicionais);
    }

    @Transactional
    public HashMap<String, Object> obterTodosAdicionaisProduto(Long idProduto){

        Produto produto = this.obterPorID(idProduto);

        HashMap<String, Object> produto_id = new HashMap<>();

        List<Adicionais> listaAddProduto = produto.getAdicionais();

        if (listaAddProduto == null) {

            listaAddProduto = new ArrayList<>();

        }

        produto_id.put("idProduto", idProduto);
        produto_id.put("Nome:", produto.getNome());
        produto_id.put("adicionais", listaAddProduto);

        return produto_id;
    }

    @Transactional
    public  Adicionais atualizarAdicionais(Long adicionaisId, Adicionais adicionaisAlterado, Usuario usuarioLogado) {
        Adicionais adicionais = adicionaisRepository.findById(adicionaisId).get();

        String[] ignoreProperties = getNullPropertyNames(adicionaisAlterado);
        List<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("produto");

        BeanUtils.copyProperties(adicionaisAlterado, adicionais, ignoreList.toArray(new String[0]));

        EntidadeAuditavelService.atualizarMetadadosEntidade(adicionais, usuarioLogado);

        adicionaisRepository.save(adicionais);

        return adicionais;
    }

    @Transactional
    public void deleteAdicionais(Long adicionaisId, Usuario usuarioLogado) {
        Adicionais adicionais = adicionaisRepository.findById(adicionaisId).get();

        EntidadeAuditavelService.desativarEntidade(adicionais, usuarioLogado);

        adicionais.setProduto(null);
        adicionaisRepository.save(adicionais);
    }
}
