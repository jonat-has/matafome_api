package br.com.ifpe.matafome_api.api.produto;

import org.hibernate.validator.constraints.Length;

import br.com.ifpe.matafome_api.modelo.produto.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {
    
    @NotNull(message = "O Nome é de preenchimento obrigatório")
    @NotBlank(message = "O Nome é de preenchimento obrigatório")
    @Length(max = 100, message = "O Nome deverá ter no máximo {max} caracteres")
    private String nome;


    private Double preco;

    @NotNull(message = "O Nome é de preenchimento obrigatório")
    @NotBlank(message = "O Nome é de preenchimento obrigatório")
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
