package br.com.ifpe.matafome_api.modelo.empresa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.api.empresa.AtualizacaoEmpresaRequest;
import br.com.ifpe.matafome_api.api.empresa.AtualizacaoEnderecoRequest;
import br.com.ifpe.matafome_api.api.empresa.Empresa_enderecoResponse;
import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.mensagens.EmailService;
import br.com.ifpe.matafome_api.modelo.pedido.Pedido;
import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Endereco_empresaRepository endereco_empresaRepository;

    @Autowired
    private UsuarioService usuarioService;

    /*Funções de empresa */


    @Transactional
    public Empresa save(Empresa empresa) throws MessagingException {

        usuarioService.save(empresa.getUsuario());
        
        Endereco_empresa enderecoSalvo = salvarEndereco_empresa(empresa.getEndereco());
        empresa.setEndereco(enderecoSalvo);

        empresa.setHabilitado(Boolean.TRUE);
        empresa.setVersao(1L);
        empresa.setDataCriacao(LocalDate.now());
        Empresa empresaSalvo = repository.save(empresa);

        new Thread(() -> {
            try {
                emailService.enviarEmailConfirmacaoCadastroEmpresa(empresaSalvo);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
 
        return empresaSalvo;
 
    }

    @Transactional
    public Endereco_empresa salvarEndereco_empresa(Endereco_empresa endereco) {

        endereco.setCep(endereco.getCep());
        endereco.setLogradouro(endereco.getLogradouro());
        endereco.setComplemento(endereco.getComplemento());
        endereco.setNumero(endereco.getNumero());
        endereco.setBairro(endereco.getBairro());
        endereco.setCidade(endereco.getCidade());
        endereco.setEstado(endereco.getEstado());


        endereco.setHabilitado(Boolean.TRUE);
        endereco.setVersao(1L);
        endereco.setDataCriacao(LocalDate.now());
        Endereco_empresa enderecoSalvo = endereco_empresaRepository.save(endereco);
 
        return enderecoSalvo;
 
    }

    public List<Empresa> listarTodos() {

        return repository.findAll();
    }

    public Empresa obterPorID(Long id) {

        Optional<Empresa> consulta = repository.findById(id);
  
       if (consulta.isPresent()) {
           return consulta.get();
       } else {
           throw new EntidadeNaoEncontradaException("empresa", id);
       }

    }

    @Transactional
    public Empresa atualizarEmpresa(Long id, AtualizacaoEmpresaRequest request) {
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("empresa", id));

        // Atualizar apenas os campos presentes no DTO
        BeanUtils.copyProperties(request, empresa, getNullPropertyNamesEmpresa(request));

        empresa.setVersao(empresa.getVersao() + 1);
        return repository.save(empresa);
    }

    // Método auxiliar para pegar nomes das propriedades nulas
    private String[] getNullPropertyNamesEmpresa(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        return Arrays.stream(pds)
                .filter(pd -> src.getPropertyValue(pd.getName()) == null)
                .map(pd -> pd.getName())
                .toArray(String[]::new);
    }

    @Transactional
    public void delete(Long id) {
    
        Empresa empresa = repository.findById(id).get();
        empresa.setHabilitado(Boolean.FALSE);
        empresa.setVersao(empresa.getVersao() + 1);
    
        repository.save(empresa);
    }




    /*Funções de Endereços de empresa */
    @Transactional
    public Endereco_empresa atualizarEndereco_empresa(Long idEmpresa, AtualizacaoEnderecoRequest request) {

        Empresa empresa = repository.findById(idEmpresa).orElseThrow(() -> new EntidadeNaoEncontradaException("empresa", idEmpresa));
        Endereco_empresa endereco = empresa.getEndereco();

        // Atualizar apenas os campos presentes no DTO
        BeanUtils.copyProperties(request, endereco, getNullPropertyNames(request));

        return endereco_empresaRepository.save(endereco);
    }

    // Método auxiliar para pegar nomes das propriedades nulas
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        return Arrays.stream(pds)
                .filter(pd -> src.getPropertyValue(pd.getName()) == null)
                .map(pd -> pd.getName())
                .toArray(String[]::new);
    }

   @Transactional
   public Empresa_enderecoResponse obterEmpresaComEndereco(Long id) {
    Empresa empresa = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("empresa", id));

    Endereco_empresa endereco = empresa.getEndereco();

    return Empresa_enderecoResponse.builder()
            .id(empresa.getId())
            .razao_social(empresa.getRazaoSocial())
            .endereco(Empresa_enderecoResponse.EnderecoResponse.builder()
                    .cep(endereco.getCep())
                    .logradouro(endereco.getLogradouro())
                    .complemento(endereco.getComplemento())
                    .numero(endereco.getNumero())
                    .bairro(endereco.getBairro())
                    .cidade(endereco.getCidade())
                    .estado(endereco.getEstado())
                    .build())
            .build();
    }

    public Empresa findByUsuarioUsername(String username) {
            return repository.findByUsuarioUsername(username)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o username: " + username));
        }

    public Page<Empresa> buscarPorNomeFantasia(String nomeFantasia, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByNomeFantasiaContainingIgnoreCase(nomeFantasia, pageable);
    }

    public Page<Empresa> buscarPorCategoria(String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCategoriaIgnoreCase(categoria, pageable);
    }

    public Page<Empresa> buscarPorNomeFantasiaECategoria(String nomeFantasia, String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByNomeFantasiaContainingIgnoreCaseAndCategoriaIgnoreCase(nomeFantasia, categoria, pageable);
    }

    @Transactional
    public HashMap<String, Object> obterTodasPrateleirasEmpresa(Long idEmpresa){

        Empresa empresa = this.obterPorID(idEmpresa);

        HashMap<String, Object> prateleiras = new HashMap<>();

        List<Prateleira> listaPrateleiras_empresa = empresa.getPrateleira();

        if (listaPrateleiras_empresa == null) {

            listaPrateleiras_empresa = new ArrayList<>();

        }

        prateleiras.put("idEmpresa", idEmpresa);
        prateleiras.put("prateleiras", listaPrateleiras_empresa);
        
        return prateleiras;

    }

    public List<Pedido> pedidoEmpresa(Long idEmpresa) {
    
    Empresa empresa = repository.findById(idEmpresa).get();
     List<Pedido> pedidosDaEmpresa = empresa.getPedidos();
     return  pedidosDaEmpresa;


    }

}
