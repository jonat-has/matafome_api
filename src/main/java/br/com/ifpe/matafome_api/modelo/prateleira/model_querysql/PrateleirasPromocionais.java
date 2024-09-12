package br.com.ifpe.matafome_api.modelo.prateleira.model_querysql;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrateleirasPromocionais {

    private Prateleira prateleira;
    private Long idEmpresa;
    private String nomeEmpresa;

}
