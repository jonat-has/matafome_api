package br.com.ifpe.matafome_api.modelo.enderecoEmpresa;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class EnderecoEmpresaService {

   @Autowired
   private EnderecoEmpresaRepository repository;

   @Transactional
   public EnderecoEmpresa save(EnderecoEmpresa enderecoEmpresa) {

      enderecoEmpresa.setHabilitado(Boolean.TRUE);
      enderecoEmpresa.setVersao(1L);
      enderecoEmpresa.setDataCriacao(LocalDate.now());
      return repository.save(enderecoEmpresa);
   }

}
