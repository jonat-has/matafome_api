package br.com.ifpe.matafome_api.api.produto;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.modelo.produto.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

    private Prateleira prateleira;

    private String nome;

    private Double preco;

    private String descricao;

    private String imagem;

    public Produto build() {
        Produto produto = Produto.builder()
                .prateleira(prateleira)
                .nome(this.nome)
                .preco(this.preco)
                .descricao(this.descricao)
                .imagem(this.imagem)
                .build();

        return produto;
    }
}
