package br.com.ifpe.matafome_api.api.empresa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Empresa_enderecoResponse {

    private Long id;
    private String razao_social;
    private EnderecoResponse endereco;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoResponse {
        private String cep;
        private String logradouro;
        private String complemento;
        private String numero;
        private String bairro;
        private String cidade;
        private String estado;
    }

}