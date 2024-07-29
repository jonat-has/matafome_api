package br.com.ifpe.matafome_api.modelo.empresa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.mensagens.EmailService;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    @Autowired
    private EmailService emailService;


    @Transactional
    public Empresa save(Empresa empresa) {

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
        empresa.setEmail(empresaAlterado.getEmail());
        empresa.setSenha(empresaAlterado.getSenha());
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


}
