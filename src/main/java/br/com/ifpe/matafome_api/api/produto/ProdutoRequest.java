package br.com.ifpe.matafome_api.api.produto;

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
    
    private String nome;

    private Double preco;

    private String categoria;

    private String descricao;

    private String imagem;

        public Produto build() {
            return Produto.builder()
                .nome(nome)
                .preco(preco)
                .categoria(categoria)
                .descricao(descricao)
                .imagem(imagem)
                .build();
            
        }
}
