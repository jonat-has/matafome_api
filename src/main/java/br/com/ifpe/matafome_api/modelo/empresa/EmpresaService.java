package br.com.ifpe.matafome_api.modelo.empresa;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import br.com.ifpe.matafome_api.api.empresa.EmpresaRequest;
import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import br.com.ifpe.matafome_api.modelo.cliente.ClienteService;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(EmpresaService.class);

    /*Funções de empresa */
    @Transactional
    public Empresa save(Empresa empresa, Usuario usuarioLogado) throws MessagingException {

        usuarioService.save(empresa.getUsuario());
        
        Endereco_empresa enderecoSalvo = salvarEndereco_empresa(empresa.getEndereco(), usuarioLogado);
        empresa.setEndereco(enderecoSalvo);

        EntidadeAuditavelService.criarMetadadosEntidade(empresa, usuarioLogado);

        empresa.setImgCapa("https://firebasestorage.googleapis.com/v0/b/upload-image-6a82d.appspot.com/o/imgCapa.jpg?alt=media&token=bc0efdd1-2bad-47a8-99cd-fcc8a7a73da7");
        empresa.setImgPerfil("https://firebasestorage.googleapis.com/v0/b/upload-image-6a82d.appspot.com/o/imgPerfil.jpg?alt=media&token=11ecea76-a46e-4d3a-9fbc-dc97736d9cb0");

        Empresa empresaSalvo = repository.save(empresa);

        new Thread(() -> {
            try {
                emailService.enviarEmailConfirmacaoCadastroEmpresa(empresaSalvo);
            } catch (MessagingException e) {
                logger.error("Erro ao enviar e-mail de confirmação para o cliente ID: {}", empresaSalvo.getId(), e);
                throw new RuntimeException("Erro ao enviar e-mail de confirmação");
            }
        }).start();
 
        return empresaSalvo;
 
    }

    @Transactional
    public Endereco_empresa salvarEndereco_empresa(Endereco_empresa endereco, Usuario usuarioLogado) {

        endereco.setCep(endereco.getCep());
        endereco.setLogradouro(endereco.getLogradouro());
        endereco.setComplemento(endereco.getComplemento());
        endereco.setNumero(endereco.getNumero());
        endereco.setBairro(endereco.getBairro());
        endereco.setCidade(endereco.getCidade());
        endereco.setEstado(endereco.getEstado());


        EntidadeAuditavelService.criarMetadadosEntidade(endereco, usuarioLogado);

        return endereco_empresaRepository.save(endereco);
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
    public Empresa atualizarEmpresa(Long id, AtualizacaoEmpresaRequest atualizacaoEmpresaRequest, Usuario usuarioLogado) {
        Empresa empresa = this.obterPorID(id);

        if (atualizacaoEmpresaRequest.getCategoria() != null) {
            empresa.setCategoria(atualizacaoEmpresaRequest.getCategoria().getCategoria());
        }

        String[] ignoreProperties = getNullPropertyNames(atualizacaoEmpresaRequest);
        List<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("usuario");

        BeanUtils.copyProperties(atualizacaoEmpresaRequest, empresa, ignoreList.toArray(new String[0]));

        EntidadeAuditavelService.atualizarMetadadosEntidade(empresa, usuarioLogado);

        repository.save(empresa);

        return empresa;
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
        Empresa empresa = this.obterPorID(id);

        EntidadeAuditavelService.desativarEntidade(empresa, usuarioLogado);
    
        repository.save(empresa);
    }




    /*Funções de Endereços de empresa */
    @Transactional
    public Endereco_empresa atualizarEndereco_empresa(Long idEmpresa, AtualizacaoEnderecoRequest request,Usuario usuarioLogado) {

        Empresa empresa = this.obterPorID(idEmpresa);
        Endereco_empresa endereco = empresa.getEndereco();

        // Atualizar apenas os campos presentes no DTO
        BeanUtils.copyProperties(request, endereco, getNullPropertyNames(request));

        EntidadeAuditavelService.atualizarMetadadosEntidade(endereco, usuarioLogado);

        return endereco_empresaRepository.save(endereco);
    }


   @Transactional
   public Empresa_enderecoResponse obterEmpresaComEndereco(Long id) {
    Empresa empresa = this.obterPorID(id);

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
        return repository.findByCategoriaContainingIgnoreCase(categoria, pageable);
    }

    public Page<Empresa> filtrarPorCategoria(String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByCategoriaContainingIgnoreCase(categoria, pageable);
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

    public Map<String, String> getCategorias() {
        return Stream.of(CategoriaEmpresaEnum.values())
                .collect(Collectors.toMap(
                        CategoriaEmpresaEnum::name, // Chave do enum
                        CategoriaEmpresaEnum::getCategoria // Descrição
                ));
    }

}
