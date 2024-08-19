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

    private String razao_social;
    private String nome_fantasia;

    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter 14 dígitos")
    private String cnpj;

    private Double taxa_frete;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O Telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    private String categoria;
    private LocalTime horario_abertura;
    private LocalTime horario_fechamento;
    private LocalTime tempo_entrega;

}