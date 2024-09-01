package br.com.ifpe.matafome_api.api.empresa;

import java.time.LocalTime;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoEmpresaRequest {

    private String razaoSocial;

    private String nomeFantasia;

    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter 14 dígitos")
    private String cnpj;

    private Double taxaFrete;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O Telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    private String categoria;

    private LocalTime horarioAbertura;

    private LocalTime horarioFechamento;

    private LocalTime tempoEntrega;

    public AtualizacaoEmpresaRequest build() {

        return AtualizacaoEmpresaRequest.builder()
                .razaoSocial(razaoSocial)
                .nomeFantasia(nomeFantasia)
                .cnpj(cnpj)
                .horarioAbertura(horarioAbertura)
                .horarioFechamento(horarioFechamento)
                .tempoEntrega(tempoEntrega)
                .taxaFrete(taxaFrete)
                .telefone(telefone)
                .categoria(categoria)
                .build();
    }

}