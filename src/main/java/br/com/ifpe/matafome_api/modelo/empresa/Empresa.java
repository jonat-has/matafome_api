package br.com.ifpe.matafome_api.modelo.empresa;

import java.time.LocalTime;
import java.util.List;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.modelo.pedido.Pedido;
import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Entity
@Table(name = "Empresa")
@SQLRestriction("habilitado = true")


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa extends EntidadeAuditavel  {
  
      @ManyToOne
      @JoinColumn(nullable = false)
      private Usuario usuario;
  
      @Column(name = "razao_social", nullable = false, length = 150)
      private String razaoSocial;
  
      @Column(name = "nome_fantasia", nullable = false, length = 100)
      private String nomeFantasia;
  
      @Column(nullable = false, unique = true, length = 14)
      private String cnpj;
  
      @Column(name = "horario_abertura", nullable = false)
      private LocalTime horarioAbertura;
  
      @Column(name = "img_capa", length = 255)
      private String imgCapa;
  
      @Column(name = "horario_fechamento", nullable = false)
      private LocalTime horarioFechamento;
  
      @Column(name = "tempo_entrega", nullable = false)
      private LocalTime tempoEntrega;
  
      @Column(name = "taxa_frete", nullable = false)
      private Double taxaFrete;
  
      @Column(length = 15)
      private String telefone;
  
      @Column(name = "img_perfil", length = 255)
      private String imgPerfil;
  
      @Column(nullable = false, length = 50)
      private String categoria;
  
      @OneToOne
      @JoinColumn(name = "endereco_id", nullable = false)
      private Endereco_empresa endereco;
  
      @OneToMany(mappedBy = "empresa", orphanRemoval = true, fetch = FetchType.EAGER)
      @Fetch(FetchMode.SUBSELECT)
      @JsonIgnore
      private List<Prateleira> prateleira;

      @OneToMany(mappedBy = "empresa", orphanRemoval = true, fetch = FetchType.EAGER)
      @Fetch(FetchMode.SUBSELECT)
      @JsonIgnore
      private List<Pedido> pedidos;

}
