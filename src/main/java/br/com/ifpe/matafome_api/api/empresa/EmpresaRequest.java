package br.com.ifpe.matafome_api.api.empresa;

import java.time.LocalTime;

import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {
   
   private String razao_social;

   private String nome_fantasia;
   
   private String cnpj;

   
   private String email;

   
   private String senha;

   
   private String horario;

   
   private String img_capa;

   
   private LocalTime tempo_entrega;

   
   private Double taxa_frete;

   
   private String telefone;

   
   private String img_perfil;

   
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