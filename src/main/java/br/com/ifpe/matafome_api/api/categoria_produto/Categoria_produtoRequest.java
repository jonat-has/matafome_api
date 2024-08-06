package br.com.ifpe.matafome_api.api.categoria_produto;

import br.com.ifpe.matafome_api.modelo.categoria_produto.Categoria_produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria_produtoRequest {

    private String nome_categoria;

        public Categoria_produto build() {

        return Categoria_produto.builder()
                .nome_categoria(nome_categoria)
                .build();
    }
}
