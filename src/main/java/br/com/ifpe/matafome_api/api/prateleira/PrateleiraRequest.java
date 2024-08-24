package br.com.ifpe.matafome_api.api.prateleira;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrateleiraRequest {

    private String nomePrateleira;

    public Prateleira build() {

        return Prateleira.builder()
                .nomePrateleira(nomePrateleira)
                .build();
    }
}
