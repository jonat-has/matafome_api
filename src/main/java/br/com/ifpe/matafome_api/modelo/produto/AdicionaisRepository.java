package br.com.ifpe.matafome_api.modelo.produto;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdicionaisRepository extends JpaRepository<Adicionais, Long> {

}