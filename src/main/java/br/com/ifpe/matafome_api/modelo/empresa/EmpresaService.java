package br.com.ifpe.matafome_api.modelo.empresa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.modelo.mensagens.EmailService;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
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
    public Empresa save(Empresa empresa) {

        usuarioService.save(empresa.getUsuario());

        empresa.setHabilitado(Boolean.TRUE);
        empresa.setVersao(1L);
        empresa.setDataCriacao(LocalDate.now());
        Empresa empresaSalvo = repository.save(empresa);

        emailService.enviarEmailConfirmacaoCadastroEmpresa(empresaSalvo);
 
        return empresaSalvo;
 
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
    public void update(Long id, Empresa empresaAlterado) {

        Empresa empresa = repository.findById(id).get();
        empresa.setRazao_social(empresaAlterado.getRazao_social());
        empresa.setNome_fantasia(empresaAlterado.getNome_fantasia());
        empresa.setCnpj(empresaAlterado.getCnpj());
        empresa.setHorario(empresaAlterado.getHorario());
        empresa.setImg_capa(empresaAlterado.getImg_capa());
        empresa.setImg_perfil(empresaAlterado.getImg_perfil());
        empresa.setTempo_entrega(empresaAlterado.getTempo_entrega());
        empresa.setTaxa_frete(empresaAlterado.getTaxa_frete());
        empresa.setTelefone(empresaAlterado.getTelefone());
        empresa.setCategoria(empresaAlterado.getCategoria());
        

        empresa.setVersao(empresa.getVersao() + 1);
        repository.save(empresa);
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
    public Endereco_empresa adicionarEndereco_empresa(Long empresaId, Endereco_empresa endereco) {

        Empresa empresa = this.obterPorID(empresaId);
        
        //Primeiro salva o Endereco_empresa:

        endereco.setEmpresa(empresa);
        endereco.setHabilitado(Boolean.TRUE);
        endereco_empresaRepository.save(endereco);
        
        //Depois acrescenta o endereço criado ao empresa e atualiza o empresa:

        List<Endereco_empresa> listaEndereco_empresa = empresa.getEnderecos();
        
        if (listaEndereco_empresa == null) {
            listaEndereco_empresa = new ArrayList<Endereco_empresa>();
        }
        
        listaEndereco_empresa.add(endereco);
        empresa.setEnderecos(listaEndereco_empresa);
        empresa.setVersao(empresa.getVersao() + 1);
        repository.save(empresa);
        
        return endereco;
    }

    
   @Transactional
   public Endereco_empresa atualizarEndereco_empresa(Long id, Endereco_empresa enderecoAlterado) {

       Endereco_empresa endereco = endereco_empresaRepository.findById(id).get();
       endereco.setNumero(enderecoAlterado.getNumero());
       endereco.setBairro(enderecoAlterado.getBairro());
       endereco.setCep(enderecoAlterado.getCep());
       endereco.setCidade(enderecoAlterado.getCidade());
       endereco.setEstado(enderecoAlterado.getEstado());
       endereco.setComplemento(enderecoAlterado.getComplemento());
       endereco.setLogradouro(enderecoAlterado.getLogradouro());

       return endereco_empresaRepository.save(endereco);
   }

   @Transactional
    public void removerEndereco_empresa(Long id) {

        Endereco_empresa endereco = endereco_empresaRepository.findById(id).get();
        endereco.setHabilitado(Boolean.FALSE);
        endereco_empresaRepository.save(endereco);

        Empresa empresa = this.obterPorID(endereco.getEmpresa().getId());
        empresa.getEnderecos().remove(endereco);
        empresa.setVersao(empresa.getVersao() + 1);
        repository.save(empresa);
    }

}
