package br.com.ifpe.matafome_api.api.empresa;

import br.com.ifpe.matafome_api.modelo.empresa.Endereco_empresa;

public class Endereco_empresaRequest {

    private String cep;

    private String logradouro;

    private String complemento;

    private String numero;

    private String bairro;
 
    private String cidade;
 
    private String estado;
 
 
    public Endereco_empresa build() {
 
        return Endereco_empresa.builder()
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
