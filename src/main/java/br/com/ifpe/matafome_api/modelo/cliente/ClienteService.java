package br.com.ifpe.matafome_api.modelo.cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private Endereco_clienteRepository endereco_clienteRepository;

    /*Funções de cliente */
    @Transactional
    public Cliente save(Cliente cliente) {

        cliente.setHabilitado(Boolean.TRUE);
        cliente.setVersao(1L);
        cliente.setDataCriacao(LocalDate.now());
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
    public void update(Long id, Cliente clienteAlterado) {

        Cliente cliente = repository.findById(id).get();
        cliente.setNome(clienteAlterado.getNome());
        cliente.setEmail(clienteAlterado.getEmail());
        cliente.setCpf(clienteAlterado.getCpf());
        cliente.setFoneCelular(clienteAlterado.getFoneCelular());
        cliente.setSenha(clienteAlterado.getSenha());

        cliente.setVersao(cliente.getVersao() + 1);
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


}
