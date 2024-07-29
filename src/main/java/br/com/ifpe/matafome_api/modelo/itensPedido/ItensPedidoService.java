package br.com.ifpe.matafome_api.modelo.itensPedido;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ItensPedidoService {

   @Autowired
   private ItensPedidoRepository repository;

   @Transactional
   public ItensPedido save(ItensPedido itensPedido) {

      itensPedido.setHabilitado(Boolean.TRUE);
      itensPedido.setVersao(1L);
      itensPedido.setDataCriacao(LocalDate.now());
      return repository.save(itensPedido);
   }

}
