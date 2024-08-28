package br.com.ifpe.matafome_api.api.produto;


import br.com.ifpe.matafome_api.modelo.produto.Adicionais;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionaisRequest {


    private String nome;


    private Float valor;


    private Integer qtd;


    private String descricao;

    public Adicionais build() {

        return Adicionais.builder()
                .nome(nome)
                .valor(valor)
                .qtd(qtd)
                .descricao(descricao)
                .build();
    }
}
