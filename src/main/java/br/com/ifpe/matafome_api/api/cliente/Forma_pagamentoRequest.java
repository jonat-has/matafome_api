package br.com.ifpe.matafome_api.api.cliente;

import br.com.ifpe.matafome_api.modelo.cliente.Forma_pagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Pattern(regexp = "\\d{16}", message = "O Número do cartão deve ter 16 dígitos")
    private String numero_cartao;

    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}", message = "A Data de validade deve estar no formato MM/AA")
    private String data_validade;

    @Size(max = 100, message = "O Nome do titular deve ter no máximo {max} caracteres")
    private String nome_titular;

    private Integer cvv;

    private String endereco_cobranca;

    private String cidade_cobranca;

    @Pattern(regexp = "[A-Z]{2}", message = "O Estado de cobrança deve ser composto por duas letras maiúsculas")
    private String estado_cobranca;

    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP de cobrança deve estar no formato XXXXX-XXX")
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
