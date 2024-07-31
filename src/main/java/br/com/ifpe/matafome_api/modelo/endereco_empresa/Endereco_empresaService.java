package br.com.ifpe.matafome_api.modelo.endereco_empresa;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class Endereco_empresaService {

   @Autowired
   private Endereco_empresaRepository repository;

   @Transactional
   public Endereco_empresa save(Endereco_empresa enderecoEmpresa) {

      enderecoEmpresa.setHabilitado(Boolean.TRUE);
      enderecoEmpresa.setVersao(1L);
      enderecoEmpresa.setDataCriacao(LocalDate.now());
      return repository.save(enderecoEmpresa);
   }

}
