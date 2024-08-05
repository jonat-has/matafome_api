package br.com.ifpe.matafome_api.modelo.pedido;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.acesso.UsuarioService;
import br.com.ifpe.matafome_api.util.exception.EntidadeNaoEncontradaException;
import jakarta.transaction.Transactional;


@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private UsuarioService usuarioService;



    /*Funções de pedido */
    @Transactional
    public Pedido save(Pedido pedido) {

        usuarioService.save(pedido.getUsuario());

        pedido.setHabilitado(Boolean.TRUE);
        pedido.setVersao(1L);
        pedido.setDataCriacao(LocalDate.now());
        Pedido pedidoSalvo = repository.save(pedido);
 
        return pedidoSalvo;
 
    }

    public List<Pedido> listarTodos() {

        return repository.findAll();
    }

    public Pedido obterPorID(Long id) {

        Optional<Pedido> consulta = repository.findById(id);
  
       if (consulta.isPresent()) {
           return consulta.get();
       } else {
           throw new EntidadeNaoEncontradaException("Pedido", id);
       }

    }

    @Transactional
    public void update(Long id, Pedido pedidoAlterado, Usuario usuarioLogado) {

        Pedido pedido = repository.findById(id).get();
        pedido.setHora_pedido(pedidoAlterado.getHora_pedido());
        pedido.setForma_pagamento(pedidoAlterado.getForma_pagamento());
        pedido.setStatus_pedido(pedidoAlterado.getStatus_pedido());
        pedido.setStatus_pedido(pedidoAlterado.getStatus_pedido());
        pedido.setStatus_pagamento(pedidoAlterado.getStatus_pagamento());
        pedido.setHorario(pedidoAlterado.getHorario());
        pedido.setValor_total(pedidoAlterado.getValor_total());
        pedido.setTaxa_entrega(pedidoAlterado.getTaxa_entrega());
        pedido.setVersao(pedido.getVersao() + 1);
        pedido.setDataUltimaModificacao(LocalDate.now());
        pedido.setUltimaModificacaoPor(usuarioLogado);
    
        repository.save(pedido);
    }

    @Transactional
    public void delete(Long id) {
    
        Pedido pedido = repository.findById(id).get();
        pedido.setHabilitado(Boolean.FALSE);
        pedido.setVersao(pedido.getVersao() + 1);
    
        repository.save(pedido);
    }


}
