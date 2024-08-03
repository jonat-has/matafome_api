package br.com.ifpe.matafome_api.modelo.cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.mensagens.EmailService;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
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


    /*Funções de cliente */
    @Transactional
    public Cliente save(Cliente cliente, Usuario usuarioLogado) {

        usuarioService.save(cliente.getUsuario());

        cliente.setHabilitado(Boolean.TRUE);
        cliente.setVersao(1L);
        cliente.setDataCriacao(LocalDate.now());
        cliente.setCriadoPor(usuarioLogado);
        Cliente clienteSalvo = repository.save(cliente);

        emailService.enviarEmailConfirmacaoCadastroCliente(clienteSalvo);
 
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
    public void update(Long id, Cliente clienteAlterado, Usuario usuarioLogado) {

        Cliente cliente = repository.findById(id).get();
        cliente.setNome(clienteAlterado.getNome());
        cliente.setCpf(clienteAlterado.getCpf());
        cliente.setFoneCelular(clienteAlterado.getFoneCelular());
        cliente.setVersao(cliente.getVersao() + 1);
        cliente.setDataUltimaModificacao(LocalDate.now());
        cliente.setUltimaModificacaoPor(usuarioLogado);
    
        repository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {
    
        Cliente cliente = repository.findById(id).get();
        cliente.setHabilitado(Boolean.FALSE);
        cliente.setVersao(cliente.getVersao() + 1);
    
        repository.save(cliente);
    }

    /*Funções de endereços de cliente */
  

    @Transactional
    public Endereco_cliente adicionarEndereco_cliente(Long clienteId, Endereco_cliente endereco) {

        Cliente cliente = this.obterPorID(clienteId);
        
        //Primeiro salva o Endereco_cliente:

        endereco.setCliente(cliente);
        endereco.setHabilitado(Boolean.TRUE);
        endereco_clienteRepository.save(endereco);
        
        //Depois acrescenta o endereço criado ao cliente e atualiza o cliente:

        List<Endereco_cliente> listaEndereco_cliente = cliente.getEnderecos();
        
        if (listaEndereco_cliente == null) {
            listaEndereco_cliente = new ArrayList<Endereco_cliente>();
        }
        
        listaEndereco_cliente.add(endereco);
        cliente.setEnderecos(listaEndereco_cliente);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
        
        return endereco;
    }

    
   @Transactional
   public Endereco_cliente atualizarEndereco_cliente(Long id, Endereco_cliente enderecoAlterado) {

       Endereco_cliente endereco = endereco_clienteRepository.findById(id).get();
       endereco.setNumero(enderecoAlterado.getNumero());
       endereco.setBairro(enderecoAlterado.getBairro());
       endereco.setCep(enderecoAlterado.getCep());
       endereco.setCidade(enderecoAlterado.getCidade());
       endereco.setEstado(enderecoAlterado.getEstado());
       endereco.setComplemento(enderecoAlterado.getComplemento());
       endereco.setLogradouro(enderecoAlterado.getLogradouro());

       return endereco_clienteRepository.save(endereco);
   }

   @Transactional
    public void removerEndereco_cliente(Long id) {

        Endereco_cliente endereco = endereco_clienteRepository.findById(id).get();
        endereco.setHabilitado(Boolean.FALSE);
        endereco_clienteRepository.save(endereco);

        Cliente cliente = this.obterPorID(endereco.getCliente().getId());
        cliente.getEnderecos().remove(endereco);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
    }

    /*Funções de formas de pagamentos */
    @Transactional
    public Forma_pagamento adicionarForma_pagamento(Long clienteId, Forma_pagamento forma_pagamento) {
 
        Cliente cliente = this.obterPorID(clienteId);
       
        //Primeiro salva o Forma_pagamento:
 
        forma_pagamento.setCliente(cliente);
        forma_pagamento.setHabilitado(Boolean.TRUE);
        forma_pagamentoRepository.save(forma_pagamento);
       
        //Depois acrescenta o endereço criado ao cliente e atualiza o cliente:
 
        List<Forma_pagamento> listaForma_pagamento = cliente.getForma_pagamento();
       
        if (listaForma_pagamento == null) {
            listaForma_pagamento = new ArrayList<Forma_pagamento>();
        }
       
        listaForma_pagamento.add(forma_pagamento);
        cliente.setForma_pagamento(listaForma_pagamento);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
       
        return forma_pagamento;
    }

    @Transactional
    public Forma_pagamento atualizarForma_pagamento(Long id, Forma_pagamento forma_pagamentoAlterado) {

        Forma_pagamento forma_pagamento = forma_pagamentoRepository.findById(id).get();
        forma_pagamento.setTipo(forma_pagamentoAlterado.getTipo());
        forma_pagamento.setNumero_cartao(forma_pagamentoAlterado.getNumero_cartao());
        forma_pagamento.setData_validade(forma_pagamentoAlterado.getData_validade());
        forma_pagamento.setNome_titular(forma_pagamentoAlterado.getNome_titular());
        forma_pagamento.setCvv(forma_pagamentoAlterado.getCvv());
        forma_pagamento.setEndereco_cobranca(forma_pagamentoAlterado.getEndereco_cobranca());
        forma_pagamento.setCidade_cobranca(forma_pagamentoAlterado.getCidade_cobranca());
        forma_pagamento.setEstado_cobranca(forma_pagamentoAlterado.getEstado_cobranca());
        forma_pagamento.setCep_cobranca(forma_pagamentoAlterado.getCep_cobranca());

        return forma_pagamentoRepository.save(forma_pagamento);
    }

    @Transactional
    public void removerForma_pagamento(Long id) {

        Forma_pagamento forma_pagamento = forma_pagamentoRepository.findById(id).get();
        forma_pagamento.setHabilitado(Boolean.FALSE);
        forma_pagamentoRepository.save(forma_pagamento);

        Cliente cliente = this.obterPorID(forma_pagamento.getCliente().getId());
        cliente.getForma_pagamento().remove(forma_pagamento);
        cliente.setVersao(cliente.getVersao() + 1);
        repository.save(cliente);
    }


}
