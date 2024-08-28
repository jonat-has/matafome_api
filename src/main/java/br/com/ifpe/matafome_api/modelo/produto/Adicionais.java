package br.com.ifpe.matafome_api.modelo.produto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.SQLRestriction;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Adicionais")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Adicionais extends EntidadeAuditavel {

    @ManyToOne
    @JsonIgnore
    private Produto produto;

    @Column
    private String nome;

    @Column
    private Float valor;

    @Column
    private Integer qtd;

    @Column
    private String descricao;

}
