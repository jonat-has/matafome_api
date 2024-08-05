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
@Table(name = "enderecoPedido")
@SQLRestriction("habilitado = true")


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoPedido extends EntidadeAuditavel  {

    @ManyToOne
      @JoinColumn(nullable = false)
      private Usuario usuario;
  
    @Column
    private String cep;

    @Column
    private String logradouro;

    @Column
    private String complemento;

    @Column
    private String numero;

    @Column
    private String ponto_de_referencia;

    @Column
    private String cidade;

    @Column
    private String estado;

    @Column
    private Integer pedido_id;

}
