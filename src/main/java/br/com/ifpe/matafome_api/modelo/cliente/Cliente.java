package br.com.ifpe.matafome_api.modelo.cliente;

import java.util.List;

import org.hibernate.annotations.SQLRestriction;

/*import br.com.ifpe.matafome_api.modelo.acesso.Usuario;*/
import br.com.ifpe.matafome_api.util.entity.EntidadeAuditavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
/*import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;*/
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Cliente")
@SQLRestriction("habilitado = true")

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends EntidadeAuditavel {

    /*@ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;*/

    @Column(nullable = false, length = 100)
    private String nome;

    @Column
    private String email;

    @Column(length = 14)
    private String foneCelular;

    @Column
    private String senha;

    @Column(unique = true)
    private String cpf;

    @OneToMany(mappedBy = "cliente", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Endereco_cliente> enderecos;

    @OneToMany(mappedBy = "cliente", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Forma_pagamento> forma_pagamento;

}
