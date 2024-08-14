package br.com.ifpe.matafome_api.modelo.produto;


import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifpe.matafome_api.modelo.prateleira.Prateleira;
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
@Table(name = "Produto")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto extends EntidadeAuditavel {

    @ManyToOne
    @JsonIgnore
    private Prateleira prateleira;

    @Column
    private String nome;

    @Column
    private Double preco;

    @Column
    private String descricao;
    
    @Column
    private String url_imagem;
}