package br.com.ifpe.matafome_api.modelo.pedido;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifpe.matafome_api.modelo.produto.Produto;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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

public class Itens_pedido extends EntidadeAuditavel {
    
    @ManyToOne
    private Produto produto;

    @ManyToOne
    @JsonIgnore
    private Pedido pedido; 

    @Column
    private Integer quantidade;


}
