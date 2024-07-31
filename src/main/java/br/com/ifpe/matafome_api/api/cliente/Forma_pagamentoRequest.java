package br.com.ifpe.matafome_api.api.cliente;

import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Forma_pagamentoRequest {

    private String tipo;

    private String numero_cartao;

    private String data_validade;

    private String nome_titular;

    private Integer cvv;

    private String endereco_cobranca;

    private String cidade_cobranca;

    private String estado_cobranca;

    private String cep_cobranca;

    public Forma_pagamento build() {
 
        return Forma_pagamento.builder()
                .tipo(tipo)
                .numero_cartao(numero_cartao)
                .data_validade(data_validade)
                .nome_titular(nome_titular)
                .cvv(cvv)
                .endereco_cobranca(endereco_cobranca)
                .cidade_cobranca(cidade_cobranca)
                .estado_cobranca(estado_cobranca)
                .cep_cobranca(cep_cobranca)
                .build();
    }
}
