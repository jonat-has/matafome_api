package br.com.ifpe.matafome_api.modelo.categoria_produto;


import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifpe.matafome_api.modelo.produto.Produto;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "Categoria")
@SQLRestriction("habilitado = true")


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria_produto extends EntidadeAuditavel {

    @Column
    private String nome_categoria;

   /*  @OneToMany
    @JsonIgnore
    private Produto produtos;
    */
}
