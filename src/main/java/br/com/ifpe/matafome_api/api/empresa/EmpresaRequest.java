package br.com.ifpe.matafome_api.api.empresa;

import java.time.LocalTime;



import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {

    @NotNull(message = "O Email é de preenchimento obrigatório")
    @NotBlank(message = "O Email é de preenchimento obrigatório")
    @Email
    private String email;

    @NotNull(message = "A Senha é de preenchimento obrigatório")
    @NotBlank(message = "A Senha é de preenchimento obrigatório")
    private String password;

    private String perfil;
   

   private String razao_social;


   private String nome_fantasia;
   
  
   private String cnpj;
   
   private String horario;

   
   private String img_capa;

   
   private String img_perfil;

   
   private LocalTime tempo_entrega;

   
   private Double taxa_frete;

 
   private String telefone;

   
   private String categoria;

      public Usuario buildUsuario() {

	return Usuario.builder()
		.username(email)
		.password(password)
		.build();
	}


   public Empresa build() {

       return Empresa.builder()
           .usuario(buildUsuario())
           .razao_social(razao_social)
           .nome_fantasia(nome_fantasia)
           .cnpj(cnpj)
           .horario(horario)
           .img_capa(img_capa)
           .tempo_entrega(tempo_entrega)
           .taxa_frete(taxa_frete)
           .telefone(telefone)
           .img_perfil(img_perfil)
           .categoria(categoria)
           .build();
   }

}