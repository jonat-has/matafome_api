package br.com.ifpe.matafome_api.modelo.itensPedido;

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
@Table(name = "ItensPedido")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ItensPedido extends EntidadeAuditavel {

    // @Column
    // private Integer produto_id_produto; este campo será ativado quando
    // configurarmos a 'chave primária'
    @Column
    private Integer qtd_produto;
    @Column
    private Double valor_unitario;
    @Column
    private double valor_total;
    @Column
    private String campo_texto;

}
