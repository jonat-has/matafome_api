package br.com.ifpe.matafome_api.api.cliente;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtualizacaoClienteRequest {

    @Length(max = 100, message = "O Nome deverá ter no máximo {max} caracteres")
    private String nome;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O Telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String foneCelular;

    @NotNull(message = "O CPF é de preenchimento obrigatório")
    @NotBlank(message = "O CPF é de preenchimento obrigatório")
    @CPF
    private String cpf;

    private String imgCliente;

    private LocalDate dataNascimento;


    public AtualizacaoClienteRequest build() {

        return AtualizacaoClienteRequest.builder()
                .nome(nome)
                .foneCelular(foneCelular)
                .cpf(cpf)
                .dataNascimento(dataNascimento)
                .imgCliente(imgCliente)
                .build();
    }


}
