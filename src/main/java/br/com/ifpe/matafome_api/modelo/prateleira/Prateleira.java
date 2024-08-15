package br.com.ifpe.matafome_api.modelo.prateleira;

import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.ifpe.matafome_api.modelo.empresa.Empresa;
import br.com.ifpe.matafome_api.modelo.produto.Produto;
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Prateleira")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prateleira extends EntidadeAuditavel {

    @ManyToOne
    @JsonIgnore
    private Empresa empresa;

    @Column
    private String nome_prateleira;

    @OneToMany(mappedBy = "prateleira", orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Produto> produtos;
    /*funciona pfv */
}
