package br.com.ifpe.matafome_api.api.cliente;

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

    private String nome;

    private String email;

    private String foneCelular;

    private String senha;

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
