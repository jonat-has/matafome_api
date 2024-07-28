package br.com.ifpe.matafome_api.api.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;


import br.com.ifpe.matafome_api.modelo.cliente.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequest {

    @NotNull(message = "O Nome é de preenchimento obrigatório")
    @NotBlank(message = "O Nome é de preenchimento obrigatório")
    @Length(max = 100, message = "O Nome deverá ter no máximo {max} caracteres")
    private String nome;


    @NotNull(message = "O Email é de preenchimento obrigatório")
    @NotBlank(message = "O Email é de preenchimento obrigatório")
    @Email
    private String email;


    @Length(min = 8, max = 20, message = "O campo Fone tem que ter entre {min} e {max} caracteres")
    private String foneCelular;

    @NotNull(message = "A Senha é de preenchimento obrigatório")
    @NotBlank(message = "A Senha é de preenchimento obrigatório")
    private String senha;

    @NotNull(message = "O CPF é de preenchimento obrigatório")
    @NotBlank(message = "O CPF é de preenchimento obrigatório")
    @CPF 
    private String cpf;


    public Cliente build() {

        return Cliente.builder()
                .nome(nome)
                .email(email)
                .foneCelular(foneCelular)
                .senha(senha)
                .cpf(cpf)
                .build();
    }

}
