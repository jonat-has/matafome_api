package br.com.ifpe.matafome_api.modelo.categoria;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CategoriaService {

   @Autowired
   private CategoriaRepository repository;

   @Transactional
   public Categoria save(Categoria categoria) {

      categoria.setHabilitado(Boolean.TRUE);
      categoria.setVersao(1L);
      categoria.setDataCriacao(LocalDate.now());
      return repository.save(categoria);
   }

}
