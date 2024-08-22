package br.com.ifpe.matafome_api.modelo.empresa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    Optional<Empresa> findByUsuarioUsername(String username);
  
}

