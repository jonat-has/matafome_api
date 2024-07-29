package br.com.ifpe.matafome_api.api.empresa;

import java.time.LocalTime;

import org.hibernate.validator.constraints.Length;

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
   
   @NotNull(message = "A razão social é de preenchimento obrigatório")
   @NotBlank(message = "A razão social e preenchimento obrigatório")
   @Length(max = 100, message = "A razão social deverá ter no máximo {max} caracteres")
   private String razao_social;

   @Length(max = 100, message = "A razão social deverá ter no máximo {max} caracteres")
   private String nome_fantasia;
   
   @NotNull(message = "O CNPJ é de preenchimento obrigatório")
   @NotBlank(message = "O CNPJ é de preenchimento obrigatório")
   private String cnpj;

   @NotNull(message = "O Email é de preenchimento obrigatório")
   @NotBlank(message = "O Email é de preenchimento obrigatório")
   @Email
   private String email;

   @NotNull(message = "A Senha é de preenchimento obrigatório")
   @NotBlank(message = "A Senha é de preenchimento obrigatório")
   private String senha;

   
   private String horario;

   
   private String img_capa;

   
   private String img_perfil;

   
   private LocalTime tempo_entrega;

   
   private Double taxa_frete;

   @Length(min = 8, max = 20, message = "O campo telefone tem que ter entre {min} e {max} caracteres")
   private String telefone;

   
   private String categoria;

   public Empresa build() {

       return Empresa.builder()
           .razao_social(razao_social)
           .nome_fantasia(nome_fantasia)
           .cnpj(cnpj)
           .email(email)
           .senha(senha)
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