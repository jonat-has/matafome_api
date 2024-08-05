package br.com.ifpe.matafome_api.modelo.pedido;

import org.hibernate.annotations.SQLRestriction;

import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "itensPedido")
@SQLRestriction("habilitado = true")


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ItensPedido extends EntidadeAuditavel {
    
    @Column
    private Integer id_produto;

    @Column
    private Integer id_pedido;

    @Column
    private Integer quantidade;

    @Column
    private Float valor;


}
