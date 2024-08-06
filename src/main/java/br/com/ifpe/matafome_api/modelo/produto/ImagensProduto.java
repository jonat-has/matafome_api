package br.com.ifpe.matafome_api.modelo.produto;

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
@Table(name = "imagensProduto")
@SQLRestriction("habilitado = true")


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImagensProduto extends EntidadeAuditavel{
    @Column
    private Integer id_produto;

    @Column
    private String url_imagem;
    
}
