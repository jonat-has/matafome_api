package br.com.ifpe.matafome_api.api.cliente;

import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco_clienteRequest {

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato XXXXX-XXX")
    private String cep;

    private String logradouro;

    private String complemento;

    private String numero;

    private String bairro;

    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "O Estado deve ser composto por duas letras maiúsculas")
    private String estado;

 
    public Endereco_cliente build() {
 
        return Endereco_cliente.builder()
                .logradouro(logradouro)
                .numero(numero)
                .bairro(bairro)
                .cep(cep)
                .cidade(cidade)
                .estado(estado)
                .complemento(complemento)
                .build();
    }
 
}
