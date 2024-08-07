package br.com.ifpe.matafome_api.modelo.prateleira;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrateleiraRepository extends JpaRepository<Prateleira, Long> {

}
