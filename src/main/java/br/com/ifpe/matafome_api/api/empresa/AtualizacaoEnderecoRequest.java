package br.com.ifpe.matafome_api.api.empresa;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoEnderecoRequest {

 
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato XXXXX-XXX")
    private String cep;

    private String logradouro;

    private String numero;

    private String bairro;

    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "O Estado deve ser composto por duas letras maiúsculas")
    private String estado;

    private String complemento;
}
