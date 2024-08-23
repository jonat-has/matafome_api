package br.com.ifpe.matafome_api.modelo.empresa;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;




public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByUsuarioUsername(String username);


    Page<Empresa> findByNomeFantasiaContainingIgnoreCase(String nomeFantasia, Pageable pageable);

    Page<Empresa> findByCategoriaIgnoreCase(String categoria, Pageable pageable);

    Page<Empresa> findByNomeFantasiaContainingIgnoreCaseAndCategoriaIgnoreCase(
            String nomeFantasia, String categoria, Pageable pageable);
  
}

