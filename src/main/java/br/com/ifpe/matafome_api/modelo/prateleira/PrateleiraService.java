package br.com.ifpe.matafome_api.modelo.prateleira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PrateleiraService {

    @Autowired
    private PrateleiraRepository prateleiraRepository;

    public Prateleira criarPrateleira(Prateleira prateleira) {
        return prateleiraRepository.save(prateleira);
    }

    public Prateleira buscarPorId(Long id) {
        Optional<Prateleira> prateleira = prateleiraRepository.findById(id);
        return prateleira.orElse(null);
    }
}
