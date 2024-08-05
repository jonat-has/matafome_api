package br.com.ifpe.matafome_api.modelo.pedido;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.matafome_api.modelo.acesso.Usuario;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statusPedido")
@SQLRestriction("habilitado = true")


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusPedido extends EntidadeAuditavel  {

    @ManyToOne
      @JoinColumn(nullable = false)
      private Usuario usuario;
  
    @Column
    private String descricao;

    @Column
    private Integer tempo_de_espera;

}

