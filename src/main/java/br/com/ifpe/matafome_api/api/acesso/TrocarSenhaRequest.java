package br.com.ifpe.matafome_api.api.acesso;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrocarSenhaRequest {

    private String email;

    private Integer codigo;
    
}
