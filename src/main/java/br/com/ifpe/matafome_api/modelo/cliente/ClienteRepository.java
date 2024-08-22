package br.com.ifpe.matafome_api.modelo.cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByUsuarioUsername(String username);
  
}

