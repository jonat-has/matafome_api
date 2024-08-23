package br.com.ifpe.matafome_api.api.empresa;

import java.time.LocalTime;
import java.util.Arrays;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.empresa.Endereco_empresa;
import jakarta.validation.constraints.Email;
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
public class EmpresaRequest {
    
   // Validação dos campos de usuário
    @NotNull(message = "O Email é de preenchimento obrigatório")
    @NotBlank(message = "O Email não pode ser vazio")
    @Email(message = "O formato do email é inválido")
    private String email;

    @NotNull(message = "A Senha é de preenchimento obrigatório")
    @NotBlank(message = "A Senha não pode ser vazia")
    @Size(min = 8, message = "A Senha deve ter pelo menos 8 caracteres")
    private String password;

    // Validação dos campos de empresa
    @NotNull(message = "A Razão Social é de preenchimento obrigatório")
    @NotBlank(message = "A Razão Social não pode ser vazia")
    private String razao_social;

    @NotNull(message = "O Nome Fantasia é de preenchimento obrigatório")
    @NotBlank(message = "O Nome Fantasia não pode ser vazio")
    private String nome_fantasia;

    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter 14 dígitos")
    private String cnpj;

    @NotNull(message = "A Taxa de Frete é de preenchimento obrigatório")
    private Double taxa_frete;

    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "O Telefone deve estar no formato (XX) XXXX-XXXX ou (XX) XXXXX-XXXX")
    private String telefone;

    // Validação dos campos de endereço
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "O CEP deve estar no formato XXXXX-XXX")
    private String cep;

    @NotNull(message = "O Logradouro é de preenchimento obrigatório")
    @NotBlank(message = "O Logradouro não pode ser vazio")
    private String logradouro;

    @NotNull(message = "O Número é de preenchimento obrigatório")
    @NotBlank(message = "O Número não pode ser vazio")
    private String numero;

    @NotNull(message = "O Bairro é de preenchimento obrigatório")
    @NotBlank(message = "O Bairro não pode ser vazio")
    private String bairro;

    @NotNull(message = "A Cidade é de preenchimento obrigatório")
    @NotBlank(message = "A Cidade não pode ser vazia")
    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "O Estado deve ser composto por duas letras maiúsculas")
    private String estado;

    private String complemento;
    private String categoria;
    private LocalTime horario_abertura;
    private LocalTime horario_fechamento;
    private String img_capa;
    private String img_perfil;
    private LocalTime tempo_entrega;


      public Usuario buildUsuario() {

        return Usuario.builder()
            .username(email)
            .password(password)
            .roles(Arrays.asList(Usuario.ROLE_EMPRESA_USER))
            .build();
	}

        public Endereco_empresa buildEnderecoEmpresa() {
        return Endereco_empresa.builder()
            .cep(cep)
            .logradouro(logradouro)
            .complemento(complemento)
            .numero(numero)
            .bairro(bairro)
            .cidade(cidade)
            .estado(estado)
            .build();
    }


   public Empresa build() {

       return Empresa.builder()
           .usuario(buildUsuario())
           .endereco(buildEnderecoEmpresa())
           .razaoSocial(razao_social)
           .nomeFantasia(nome_fantasia)
           .cnpj(cnpj)
           .horarioAbertura(horario_abertura)
           .horarioFechamento(horario_fechamento)
           .imgCapa(img_capa)
           .tempoEntrega(tempo_entrega)
           .taxaFrete(taxa_frete)
           .telefone(telefone)
           .imgPerfil(img_perfil)
           .categoria(categoria)
           .build();
   }

}