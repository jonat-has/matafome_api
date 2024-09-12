package br.com.ifpe.matafome_api.modelo.cliente;

import java.beans.PropertyDescriptor;
import java.util.*;

import br.com.ifpe.matafome_api.api.cliente.AtualizacaoClienteRequest;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavelService;
import org.slf4j.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.mensagens.EmailService;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private Endereco_clienteRepository endereco_clienteRepository;

    @Autowired
    private Forma_pagamentoRepository forma_pagamentoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    /*Funções de cliente */
    @Transactional
    public Cliente save(Cliente cliente, Usuario usuarioLogado) throws MessagingException {

        usuarioService.save(cliente.getUsuario());

        EntidadeAuditavelService.criarMetadadosEntidade(cliente, usuarioLogado);

        Cliente clienteSalvo = repository.save(cliente);
        
        new Thread(() -> {
            try {
                emailService.enviarEmailConfirmacaoCadastroCliente(clienteSalvo);
            } catch (MessagingException e) {
                logger.error("Erro ao enviar e-mail de confirmação para o cliente ID: {}", clienteSalvo.getId(), e);
                throw new RuntimeException("Erro ao enviar e-mail de confirmação");
            }
        }).start();
 
 
        return clienteSalvo;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente obterPorID(Long id) {
        Optional<Cliente> consulta = repository.findById(id);
       if (consulta.isPresent()) {
           return consulta.get();
       } else {
           throw new EntidadeNaoEncontradaException("Cliente", id);
       }
    }

    @Transactional
    public Cliente update(Long id, AtualizacaoClienteRequest clienteAlterado, Usuario usuarioLogado) {

        Cliente cliente = this.obterPorID(id);

        String[] ignoreProperties = getNullPropertyNames(clienteAlterado);
        List<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("usuario");

        BeanUtils.copyProperties(clienteAlterado, cliente, ignoreList.toArray(new String[0]));

        EntidadeAuditavelService.atualizarMetadadosEntidade(cliente, usuarioLogado);
    
        repository.save(cliente);

        return cliente;
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
    
        Cliente cliente = this.obterPorID(id);

        EntidadeAuditavelService.desativarEntidade(cliente, usuarioLogado);
    
        repository.save(cliente);
    }

    /*Funções de endereços de cliente */

    @Transactional
    public HashMap<String, Object> obterTodosEnderecosCliente(Long idCliente){

        Cliente cliente = this.obterPorID(idCliente);

        HashMap<String, Object> enderecosPlusId = new HashMap<>();

        List<Endereco_cliente> listaEndereco_cliente = cliente.getEnderecos();

        if (listaEndereco_cliente == null) {
            listaEndereco_cliente = new ArrayList<>();
        }

        enderecosPlusId.put("idCliente", idCliente);
        enderecosPlusId.put("enderecos", listaEndereco_cliente);

        return enderecosPlusId;
    }
  

    @Transactional
    public Endereco_cliente adicionarEndereco_cliente(Long clienteId, Endereco_cliente endereco, Usuario usuarioLogado) {

        Cliente cliente = this.obterPorID(clienteId);

        endereco.setCliente(cliente);
        EntidadeAuditavelService.criarMetadadosEntidade(endereco, usuarioLogado);
        endereco_clienteRepository.save(endereco);

        List<Endereco_cliente> listaEndereco_cliente = cliente.getEnderecos();
        
        if (listaEndereco_cliente == null) {
            listaEndereco_cliente = new ArrayList<>();
        }
        
        listaEndereco_cliente.add(endereco);
        cliente.setEnderecos(listaEndereco_cliente);

        EntidadeAuditavelService.atualizarMetadadosEntidade(cliente, usuarioLogado);
        repository.save(cliente);
        
        return endereco;
    }

    
   @Transactional
   public Endereco_cliente atualizarEndereco_cliente(Long id, Endereco_cliente enderecoAlterado,Usuario usuarioLogado) {

       Endereco_cliente endereco = endereco_clienteRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

       // Copia as propriedades não nulas de enderecoAlterado para endereco
       BeanUtils.copyProperties(enderecoAlterado, endereco, getNullPropertyNames(enderecoAlterado));

       EntidadeAuditavelService.atualizarMetadadosEntidade(endereco, usuarioLogado);

       return endereco_clienteRepository.save(endereco);
   }

   @Transactional
    public void removerEndereco_cliente(Long id, Usuario usuarioLogado) {

        Endereco_cliente endereco = endereco_clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

        EntidadeAuditavelService.desativarEntidade(endereco, usuarioLogado);

        endereco_clienteRepository.save(endereco);

        Cliente cliente = this.obterPorID(endereco.getCliente().getId());
        cliente.getEnderecos().remove(endereco);

        EntidadeAuditavelService.atualizarMetadadosEntidade(cliente, usuarioLogado);

        repository.save(cliente);
    }

    /*Funções de formas de pagamentos */

    @Transactional
    public HashMap<String, Object> obterTodasFormasPagCliente(Long idCliente){

        Cliente cliente = this.obterPorID(idCliente);

        HashMap<String, Object> PagPlusId = new HashMap<>();

        List<Forma_pagamento> listaPag_cliente = cliente.getForma_pagamento();

        if (listaPag_cliente == null) {

            listaPag_cliente = new ArrayList<>();

        }

        PagPlusId.put("idCliente", idCliente);
        PagPlusId.put("pagtos", listaPag_cliente);

        return PagPlusId;

    }

    @Transactional
    public Forma_pagamento adicionarForma_pagamento(Long clienteId, Forma_pagamento forma_pagamento, Usuario usuarioLogado) {
 
        Cliente cliente = this.obterPorID(clienteId);

        forma_pagamento.setCliente(cliente);

        EntidadeAuditavelService.criarMetadadosEntidade(forma_pagamento, usuarioLogado);

        forma_pagamentoRepository.save(forma_pagamento);
 
        List<Forma_pagamento> listaForma_pagamento = cliente.getForma_pagamento();
       
        if (listaForma_pagamento == null) {
            listaForma_pagamento = new ArrayList<>();
        }
       
        listaForma_pagamento.add(forma_pagamento);
        cliente.setForma_pagamento(listaForma_pagamento);

        EntidadeAuditavelService.atualizarMetadadosEntidade(cliente, usuarioLogado);

        repository.save(cliente);
       
        return forma_pagamento;
    }

    @Transactional
    public Forma_pagamento atualizarForma_pagamento(Long id, Forma_pagamento forma_pagamentoAlterado, Usuario usuarioLogado) {
        Forma_pagamento forma_pagamento = forma_pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        // Copia as propriedades não nulas de forma_pagamentoAlterado para forma_pagamento
        BeanUtils.copyProperties(forma_pagamentoAlterado, forma_pagamento, getNullPropertyNames(forma_pagamentoAlterado));

        EntidadeAuditavelService.atualizarMetadadosEntidade(forma_pagamento, usuarioLogado);

        return forma_pagamentoRepository.save(forma_pagamento);
    }

    @Transactional
    public void removerForma_pagamento(Long id, Usuario usuarioLogado) {

        Forma_pagamento forma_pagamento = forma_pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

        EntidadeAuditavelService.desativarEntidade(forma_pagamento, usuarioLogado);

        forma_pagamentoRepository.save(forma_pagamento);

        Cliente cliente = this.obterPorID(forma_pagamento.getCliente().getId());

        EntidadeAuditavelService.atualizarMetadadosEntidade(cliente, usuarioLogado);

        repository.save(cliente);
    }

    public Cliente findByUsuarioUsername(String username) {
        return repository.findByUsuarioUsername(username)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o username: " + username));
    }

}
