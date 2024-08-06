package br.com.ifpe.matafome_api.api.produto;


import br.com.ifpe.matafome_api.modelo.categoria_produto.Categoria_produto;
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

    private Categoria_produto categoria;

    private String nome;

    private Double preco;

    private String descricao;

    private String imagem;

    public Produto build() {
        Produto produto = Produto.builder()
            .nome(this.nome)
            .preco(this.preco)
            .descricao(this.descricao)
            .imagem(this.imagem)
            .build();

        return produto;
    }
}
