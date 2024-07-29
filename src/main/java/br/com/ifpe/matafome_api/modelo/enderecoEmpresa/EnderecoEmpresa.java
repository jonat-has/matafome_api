package br.com.ifpe.matafome_api.modelo.enderecoEmpresa;

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
@Table(name = "EnderecoEmpresa")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EnderecoEmpresa extends EntidadeAuditavel {

    @Column
    private String rua;
    @Column
    private String cidade;
    @Column
    private String bairro;
    @Column
    private String estado;
    @Column
    private String cep;
    @Column
    private String numero;

}
