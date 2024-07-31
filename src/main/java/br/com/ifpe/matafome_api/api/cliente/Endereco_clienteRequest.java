package br.com.ifpe.matafome_api.api.cliente;

import br.com.ifpe.matafome_api.modelo.cliente.Endereco_cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco_clienteRequest {

    private String cep;

    private String logradouro;

    private String complemento;

    private String numero;

    private String bairro;
 
    private String cidade;
 
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
