package br.com.ifpe.matafome_api.modelo.empresa;

import java.time.LocalTime;
import java.util.List;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
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
import jakarta.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;



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

      @Column
      private String razao_social;

      @Column
      private String nome_fantasia;

      @Column
      private String cnpj;

      @Column
      private String horario;

      @Column
      private String img_capa;

      @Column
      private LocalTime tempo_entrega;

      @Column
      private Double taxa_frete;

      @Column
      private String telefone;

      @Column
      private String img_perfil;

      @Column
      private String categoria;

      @OneToMany(mappedBy = "empresa", orphanRemoval = true, fetch = FetchType.EAGER)
      @Fetch(FetchMode.SUBSELECT)
      private List<Endereco_empresa> enderecos;

}
